package com.example.scoutai

import CameraViewModel
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.foundation.layout.padding

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.scoutai.ui.theme.ScoutAITheme



class FunctionalActivity : ComponentActivity() {
    private val cameraViewModel: CameraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScoutAITheme {
                var showDialog by remember { mutableStateOf(true) }

                if (showDialog) {
                    PermissionAndConfigDialog(
                        cameraViewModel = cameraViewModel,  // Pass the cameraViewModel here
                        onDismiss = { showDialog = false },
                        onSave = { newInterval, newImages, newGps, newOrientation ->
                            // Update cameraViewModel state using its update functions
                            cameraViewModel.updatePermissions(newImages, newGps, newOrientation)
                            //cameraViewModel.updateRecordTimestamps(newTimestamps)
                            cameraViewModel.updateCaptureInterval(newInterval)  // Update the interval in ViewModel
                            showDialog = false
                        }
                    )
                }

                AppNavigation(
                    cameraViewModel,  // Pass the cameraViewModel instead of separate values
                )
            }
        }
    }
}



@Composable
fun AppNavigation(
    cameraViewModel: CameraViewModel,
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "camera",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("camera") {
                CameraFragment(
                    cameraViewModel
                )
            }
            composable("second") {
                SecondScreenFrag(
                    cameraViewModel,
                    navController
                )
            }
        }
    }
}


// CapturedImage data class
data class CapturedImage(
    val name: String,
    val uri: String,
    val orientation: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)




