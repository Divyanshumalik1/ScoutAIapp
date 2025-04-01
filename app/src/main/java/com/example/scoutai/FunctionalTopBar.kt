package com.example.scoutai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun FunctionalTopBar(
    onStart: () -> Unit,
    onStop: () -> Unit,
    onConfigure: () -> Unit,
    imagesPermission: Boolean,  // Define expected parameter names
    gpsPermission: Boolean,
    orientationPermission: Boolean,
    captureInterval: Long,
    recordTimestamps: Boolean
) {
    // Default color for buttons
    val defaultColor = Color.White

    // States to track button colors locally
    val startButtonColor = remember { mutableStateOf(defaultColor) }
    val stopButtonColor = remember { mutableStateOf(defaultColor) }

//    // States for local tracking of changes
//    var imagesPermission by remember { mutableStateOf(initialImagesPermission) }
//    var gpsPermission by remember { mutableStateOf(initialGpsPermission) }
//    var orientationPermission by remember { mutableStateOf(initialOrientationPermission) }
//    var captureInterval by remember { mutableStateOf(initialCaptureInterval) }
//    var recordTimestamps by remember { mutableStateOf(initialRecordTimestamps) }

    // Permission statuses display text (local state display)
    val imagesPermissionStatus = if (imagesPermission) "Images: Granted" else "Images: Denied"
    val gpsPermissionStatus = if (gpsPermission) "GPS: Granted" else "GPS: Denied"
    val orientationPermissionStatus = if (orientationPermission) "Orientation: Granted" else "Orientation: Denied"

    // Row for buttons
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Start Button
        Button(
            onClick = {
                onStart()
                startButtonColor.value = Color.Green
                stopButtonColor.value = defaultColor
            },
            colors = ButtonDefaults.buttonColors(containerColor = startButtonColor.value)
        ) {
            Text("Start")
        }

        // Stop Button
        Button(
            onClick = {
                onStop()
                stopButtonColor.value = Color.Red
                startButtonColor.value = defaultColor
            },
            colors = ButtonDefaults.buttonColors(containerColor = stopButtonColor.value)
        ) {
            Text("Stop")
        }

        // Configure Button
        Button(
            onClick = onConfigure,  // Open the configuration dialog
            modifier = Modifier
                .padding(start = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Configure")
        }
    }

    // Display local permission statuses and other information
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = imagesPermissionStatus)
        Text(text = gpsPermissionStatus)
        Text(text = orientationPermissionStatus)

        // Show capture interval in seconds
        Text(
            text = "Capture Interval: ${captureInterval / 1000} seconds",
        )

//        // Show timestamp status
//        Text(
//            text = "Timestamps: ${if (recordTimestamps) "Enabled" else "Disabled"}",
//        )
    }
}
