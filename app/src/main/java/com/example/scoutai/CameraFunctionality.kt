package com.example.scoutai

import CameraViewModel
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import getCurrentLocation
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun CameraFragment(
    cameraViewModel: CameraViewModel,
) {
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    var cameraProvider: ProcessCameraProvider? by remember { mutableStateOf(null) }
    var isCapturing by remember { mutableStateOf(false) }
    var isStopped by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val captureInterval by cameraViewModel.captureInterval.collectAsState()
    val imagesPermission by cameraViewModel.imagesPermission.collectAsState()
    val gpsPermission by cameraViewModel.gpsPermission.collectAsState()
    val orientationPermission by cameraViewModel.orientationPermission.collectAsState()
    val recordTimestamps by cameraViewModel.recordTimestamps.collectAsState()

    // Check for camera permission before initializing
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                try {
                    cameraProvider = cameraProviderFuture.get()
                } catch (e: Exception) {
                    Log.e("CameraFragment", "Camera initialization failed", e)
                    errorMessage = "Camera initialization failed: ${e.message}"
                }
            }, ContextCompat.getMainExecutor(context))
        } else {
            ActivityCompat.requestPermissions(
                context as ComponentActivity,
                arrayOf(Manifest.permission.CAMERA),
                1001
            )
        }
    }

    // Start capturing photos periodically with updated interval
    LaunchedEffect(isCapturing, isStopped, captureInterval) {
        if (isCapturing && !isStopped && imageCapture != null) {
            while (!isStopped && isCapturing) {
                delay(captureInterval)
                takePhoto(cameraViewModel, context, imageCapture)
            }
        }
    }

    // Set up camera preview once the provider is available
    LaunchedEffect(cameraProvider) {
        cameraProvider?.let { provider ->
            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                provider.unbindAll()
                provider.bindToLifecycle(context as ComponentActivity, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.e("CameraFragment", "Binding camera use cases failed", e)
                errorMessage = "Binding camera use cases failed: ${e.message}"
            }
        }
    }

    // UI layout for Camera Fragment
    Column(modifier = Modifier.fillMaxSize()) {
        FunctionalTopBar(
            onStart = {
                Toast.makeText(context, "Capturing started!", Toast.LENGTH_SHORT).show()
                isCapturing = true
                isStopped = false
            },
            onStop = {
                Toast.makeText(context, "Capturing stopped!", Toast.LENGTH_SHORT).show()
                isCapturing = false
                isStopped = true
            },
            onConfigure = { showDialog = true },
            imagesPermission = imagesPermission,
            gpsPermission = gpsPermission,
            orientationPermission = orientationPermission,
            captureInterval = captureInterval,
            recordTimestamps = recordTimestamps
        )

        if (showDialog) {
            // Configuration Dialog
            PermissionAndConfigDialog(
                cameraViewModel = cameraViewModel,  // Pass the cameraViewModel here
                onDismiss = { showDialog = false },
                onSave = { newInterval, newImages, newGps, newOrientation ->
                    // Update cameraViewModel state
                    cameraViewModel.updatePermissions(newImages, newGps, newOrientation)
                    //cameraViewModel.updateRecordTimestamps(newTimestamps)
                    cameraViewModel.updateCaptureInterval(newInterval)  // Update the interval locally
                    showDialog = false
                }
            )

        }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = { previewView })

            errorMessage?.let {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {
                        TextButton(onClick = { errorMessage = null }) {
                            Text("Dismiss")
                        }
                    }
                ) {
                    Text(it)
                }
            }

            // Floating Capture Button
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Button(
                    onClick = { takePhoto(cameraViewModel, context, imageCapture) },
                    modifier = Modifier
                        .padding(32.dp)
                        .size(70.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = CircleShape
                ) {}
            }
        }
    }
}

// Take photo function
private fun takePhoto(cameraViewModel: CameraViewModel, context: Context, imageCapture: ImageCapture?) {
    if (imageCapture == null) return

    val fileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) + ".jpg"
    val outputFile = File(context.getExternalFilesDir(null), fileName)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

    val executor = ContextCompat.getMainExecutor(context)

    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = outputFile.toURI().toString()
            val name = "Image_${System.currentTimeMillis()}"

            // Capture orientation data
            val orientationSensor = OrientationSensor(context)
            orientationSensor.startListening { azimuth, pitch, roll ->
                val orientation = "Azimuth: $azimuth°, Pitch: $pitch°, Roll: $roll°"

                // Fetch current GPS coordinates
                getCurrentLocation(context) { latitude, longitude ->
                    if (latitude != null && longitude != null) {
                        Log.d("CameraFragment", "Location fetched - Latitude: $latitude, Longitude: $longitude")
                    } else {
                        Log.e("CameraFragment", "Failed to fetch location")
                    }

                    // Add image with GPS & orientation data to ViewModel
                    cameraViewModel.addImage(savedUri, name, orientation, latitude, longitude)

                    Toast.makeText(context, "Image Captured!", Toast.LENGTH_SHORT).show()

                    // Stop orientation sensor once data is captured
                    orientationSensor.stopListening()
                }
            }
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraFragment", "Image capture failed: ${exception.message}")
        }
    })
}