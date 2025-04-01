package com.example.scoutai

import CameraViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter

@Composable
fun PermissionAndConfigDialog(
    cameraViewModel: CameraViewModel,
    onDismiss: () -> Unit,
    onSave: (Long, Boolean, Boolean, Boolean) -> Unit
) {
    val captureInterval by cameraViewModel.captureInterval.collectAsState()
    val imagesPermissionState by cameraViewModel.imagesPermission.collectAsState()
    val gpsPermissionState by cameraViewModel.gpsPermission.collectAsState()
    val orientationPermissionState by cameraViewModel.orientationPermission.collectAsState()
    val recordTimestampsState by cameraViewModel.recordTimestamps.collectAsState()

    // Create mutable state variables for the dialog fields
    var localImagesPermission by remember { mutableStateOf(imagesPermissionState) }
    var localGpsPermission by remember { mutableStateOf(gpsPermissionState) }
    var localOrientationPermission by remember { mutableStateOf(orientationPermissionState) }
    var localRecordTimestampsState by remember { mutableStateOf(recordTimestampsState) }

    var isError by remember { mutableStateOf(false) }
    var delayText by remember { mutableStateOf(captureInterval.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Configurations") },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Choose Configuration", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                // Permissions Section with Green Checkboxes
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = localImagesPermission,
                        onCheckedChange = { localImagesPermission = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50), uncheckedColor = Color.Gray)
                    )
                    Text("Images")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = localGpsPermission,
                        onCheckedChange = { localGpsPermission = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50), uncheckedColor = Color.Gray)
                    )
                    Text("GPS")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = localOrientationPermission,
                        onCheckedChange = { localOrientationPermission = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50), uncheckedColor = Color.Gray)
                    )
                    Text("Orientation")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Capture Interval Section
                Text("Set Capture Interval (in milliseconds)", fontWeight = FontWeight.Bold)
                TextField(
                    value = delayText,
                    onValueChange = { input ->
                        delayText = input
                        isError = input.toLongOrNull() == null || input.toLongOrNull()?.let { it <= 0 } == true
                    },
                    label = { Text("Capture Interval") },
                    isError = isError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (isError) {
                    Text("Please enter a valid positive number", color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val delayValue = delayText.toLongOrNull() ?: captureInterval
                    if (!isError) {
                        onSave(
                            delayValue,
                            localImagesPermission,
                            localGpsPermission,
                            localOrientationPermission
                        )
                        onDismiss()
                    }
                },
                enabled = !isError,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Save", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}


@Composable
fun SecondScreenFrag(cameraViewModel: CameraViewModel, navController: NavController) {
    // Use cameraViewModel to manage permission states dynamically
    val imagesPermission by cameraViewModel.imagesPermission.collectAsState()
    val gpsPermission by cameraViewModel.gpsPermission.collectAsState()
    val orientationPermission by cameraViewModel.orientationPermission.collectAsState()

    // State to control which view is visible
    val (showTable, setShowTable) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Captured Data", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))

        // Check if there are any images captured
        if (cameraViewModel.imagesList.isEmpty()) {
            Text("No Data captured yet", fontSize = 16.sp)
        } else {
            // Button to toggle between the Image List and the Table
            Button(
                onClick = { setShowTable(!showTable) },
                modifier = Modifier.padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(if (showTable) "View Image List" else "View Captured Image Table")
            }

            // Conditionally display the ImageListView or CapturedImageTable based on the `showTable` state
            if (showTable) {
                // Display CapturedImageTable
                CapturedImageTable(context = LocalContext.current, images = cameraViewModel.imagesList)
            } else {
                // Display ImageListView
                ImageListView(
                    images = cameraViewModel.imagesList,
                    imagesPermission = imagesPermission,
                    gpsPermission = gpsPermission,
                    orientationPermission = orientationPermission
                )
            }
        }
    }
}



@Composable
fun ImageListView(
    images: List<CapturedImage>,
    imagesPermission: Boolean,
    gpsPermission: Boolean,
    orientationPermission: Boolean
) {
    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(images) { image ->
            val (imageName, imageUri, orientation, latitude, longitude) = image // Assuming CapturedImage includes latitude and longitude

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable {
                        // Only clickable if image permission is granted
                        if (imagesPermission) {
                            selectedImageUri = imageUri // Set the selected image URI for full-screen view
                        }
                    },
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)) // Green card color
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    // Display the image if permission allows
                    if (imagesPermission) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.White)
                        )
                    } else {
                        // Fallback if images permission is denied
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Image Permission Denied", color = Color.LightGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Displaying the image name
                    Text("Image: ${image.name}", modifier = Modifier.padding(top = 8.dp), color = Color.Black)

                    // Displaying the orientation if permission allows
                    if (orientationPermission) {
                        Text(
                            "Orientation: $orientation",
                            modifier = Modifier.padding(top = 4.dp), color = Color.Black
                        )
                    } else {
                        Text(
                            "Orientation: Permission Denied",
                            modifier = Modifier.padding(top = 4.dp),
                            color = Color.Gray
                        )
                    }

                    // Displaying the location (latitude/longitude) if permission allows
                    if (gpsPermission) {
                        latitude?.let {
                            Text(
                                "Latitude: $it",
                                modifier = Modifier.padding(top = 4.dp), color = Color.Black
                            )
                        }

                        longitude?.let {
                            Text(
                                "Longitude: $it",
                                modifier = Modifier.padding(top = 4.dp), color = Color.Black
                            )
                        }
                    } else {
                        Text(
                            "Location: Permission Denied",
                            modifier = Modifier.padding(top = 4.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

    // If there's an image selected, show the full-screen dialog
    selectedImageUri?.let {
        FullScreenImageDialog(
            imageUri = it,
            onDismiss = { selectedImageUri = null }
        )
    }
}
