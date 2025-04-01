package com.example.scoutai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.scoutai.ui.theme.ScoutAITheme
import androidx.compose.runtime.mutableStateOf

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp



class SecondScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScoutAITheme {
                // Commenting out all logic except for navigation to FunctionalActivity
                Column(
                    modifier = Modifier
                        .background(Color(0xFF4CAF50))
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Instructions",
                        modifier = Modifier.padding(bottom = 16.dp),
                        style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)  // Increased font size and bold heading
                    )
                    Text(
                        text = "Follow the instructions below to use the app:\n\n" +
                                "1. Capture images using your device's camera.\n" +
                                "2. Choose which types of data to collect (e.g., image, GPS location, orientation).\n" +
                                "3. Ensure that your device has granted internet and location permissions.\n" +
                                "4. Configure the frequency for data collection, such as how often location or orientation is sampled.\n" +
                                "5. Press the button below to capture data manually.\n" +
                                "6. Start recording your session to begin capturing data.\n" +
                                "7. Stop recording when finished, and preview or export your recorded data as needed.",
                        modifier = Modifier.padding(bottom = 32.dp),
                        style = TextStyle(fontSize = 18.sp)  // Increased text size for body text
                    )

                    // "Next" button that navigates to FunctionalActivity
                    Button(
                        onClick = {
                            val intent = Intent(this@SecondScreen, FunctionalActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .height(48.dp)
                            .width(200.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF228B22)) // Correct placement
                    ) {
                        Text(text = "Next", color = Color.White)
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SecondScreenPreview() {
    ScoutAITheme {
        SecondScreen()
    }
}


//
//@Composable
//fun SecondScreenPreview() {
//    ScoutAITheme {
//        // Simulate the state with openDialog set to true for previewing the dialog
//        var openDialog by remember { mutableStateOf(true) }
//        var currentDelay by remember { mutableStateOf(2000L) } // Default capture interval
//        var imagesPermission by remember { mutableStateOf(false) }
//        var gpsPermission by remember { mutableStateOf(false) }
//        var orientationPermission by remember { mutableStateOf(false) }
//        var recordTimestamps by remember { mutableStateOf(false) }
//
//        if (openDialog) {
//            PermissionAndConfigDialog(
//                currentDelay = currentDelay,
//                imagesPermission = imagesPermission,
//                gpsPermission = gpsPermission,
//                orientationPermission = orientationPermission,
//                recordTimestamps = recordTimestamps,
//                onDismiss = { openDialog = false },
//                onSave = { delay, images, gps, orientation, timestamps ->
//                    currentDelay = delay
//                    imagesPermission = images
//                    gpsPermission = gps
//                    orientationPermission = orientation
//                    recordTimestamps = timestamps
//                }
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PermissionAndConfigDialogPreview() {
//    ScoutAITheme {
//        // Preview the dialog with some default values
//        PermissionAndConfigDialog(
//            currentDelay = 1000L,
//            imagesPermission = false,
//            gpsPermission = false,
//            orientationPermission = false,
//            recordTimestamps = false,
//            onDismiss = {},
//            onSave = { _, _, _, _, _ -> }
//        )
//    }
//}

