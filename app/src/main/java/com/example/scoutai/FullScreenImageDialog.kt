package com.example.scoutai

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.ui.unit.IntOffset

//import androidx.navigation.compose.navArgument


@Composable
fun FullScreenImageDialog(imageUri: String, onDismiss: () -> Unit) {
    var scale by remember { mutableStateOf(1f) } // Initial scale factor for zoom
    var offset by remember { mutableStateOf(Offset(0f, 0f)) } // Initial position of the image
    var previousPan by remember { mutableStateOf(Offset(0f, 0f)) } // To track the previous position for drag

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Fullscreen box
                .background(Color.Black)
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Full Screen Image",
                modifier = Modifier
                    .wrapContentSize() // Ensure that the image size adapts to the content
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
                    .offset { // Apply dragging offset to move the image
                        IntOffset(offset.x.toInt(), offset.y.toInt())
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            // Zooming functionality
                            scale *= zoom
                            scale = scale.coerceIn(0.5f, 3f) // Limit the zoom range

                            // Moving functionality
                            offset = Offset(
                                offset.x + pan.x - previousPan.x,
                                offset.y + pan.y - previousPan.y
                            )

                            // Update previous pan to track movement
                            previousPan = pan
                        }
                    }
                    .align(Alignment.Center)
            )

            // Close button to dismiss the dialog
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50)) // Green background color
            ) {
                Text("Close", color = Color.White) // White text on the button
            }
        }
    }
}



