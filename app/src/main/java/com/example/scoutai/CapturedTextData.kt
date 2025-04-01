package com.example.scoutai

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

@Composable
fun CapturedImageTable(context: Context, images: List<CapturedImage>) {
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(verticalScrollState)
    ) {
        Text("Captured Image Data", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))

        // Button for downloading the text file
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { saveTextFile(context, images) },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green color for the button
            ) {
                Text("Download Text File", color = Color.White) // White text on the green button
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Table Layout with horizontal scrolling
        Row(
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
                .padding(vertical = 8.dp)
        ) {
            Column {
                // Table Header
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    listOf("Image Name", "Latitude", "Longitude", "Azimuth", "Pitch", "Roll").forEach {
                        Text(text = it, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                // Table Rows
                images.forEach { image ->
                    val (azimuth, pitch, roll) = parseOrientation(image.orientation)

                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        listOf(
                            image.name,
                            image.latitude?.toString() ?: "N/A",
                            image.longitude?.toString() ?: "N/A",
                            azimuth,
                            pitch,
                            roll
                        ).forEach {
                            Text(text = it, modifier = Modifier.padding(8.dp), fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

// Function to save the table as a text file
fun saveTextFile(context: Context, images: List<CapturedImage>) {
    val textFile = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "CapturedData.txt"
    )

    try {
        val writer = FileWriter(textFile)
        val bufferedWriter = BufferedWriter(writer)

        // Write the header
        bufferedWriter.write("Captured Image Data\n")
        bufferedWriter.write("Image Name, Latitude, Longitude, Azimuth, Pitch, Roll\n")

        // Iterate through the images and write data
        images.forEach { image ->
            val (azimuth, pitch, roll) = parseOrientation(image.orientation)
            val text = "${image.name}, ${image.latitude ?: "N/A"}, ${image.longitude ?: "N/A"}, $azimuth, $pitch, $roll"
            bufferedWriter.write(text)
            bufferedWriter.newLine()
        }

        bufferedWriter.close()
        Toast.makeText(context, "Text file saved to Downloads: ${textFile.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving text file", Toast.LENGTH_LONG).show()
    }
}

// Function to parse azimuth, pitch, and roll from orientation string
fun parseOrientation(orientation: String?): Triple<String, String, String> {
    return orientation?.split(",")?.map { it.trim() }?.let { parts ->
        Triple(
            parts.getOrNull(0) ?: "N/A",
            parts.getOrNull(1) ?: "N/A",
            parts.getOrNull(2) ?: "N/A"
        )
    } ?: Triple("N/A", "N/A", "N/A")
}

