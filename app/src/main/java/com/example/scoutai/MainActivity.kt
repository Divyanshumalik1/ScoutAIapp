package com.example.scoutai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scoutai.ui.theme.ScoutAITheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScoutAITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InitialScreen(
                        modifier = Modifier.padding(innerPadding),
                        onGetStartedClick = {
                            // Start the SecondScreen Activity using Intent
                            val intent = Intent(this, SecondScreen::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InitialScreen(modifier: Modifier = Modifier, onGetStartedClick: () -> Unit) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(Color(0xFF4CAF50))// Ensure the Box fills the entire screen
    ) {
        // Full-Screen Background Image
        Image(
            painter = painterResource(id = R.drawable.scouta3), // Replace with your actual image resource
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize()  // Ensure the image fills the entire screen
        )

        // UI elements on top of the background image
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),  // Adjust padding as needed
            verticalArrangement = Arrangement.Center,  // Center the content vertically
            horizontalAlignment = Alignment.CenterHorizontally  // Center the content horizontally
        ) {
            // Centered Image
            Image(
                painter = painterResource(id = R.drawable.scut), // Replace with your actual image resource
                contentDescription = "Center Image",
                modifier = Modifier.size(250.dp)
                      // Adjust size as needed
                    .clip(RoundedCornerShape(16.dp))// Adjust size as needed
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Get Started Button
            Button(
                onClick = onGetStartedClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(48.dp)
                    .width(200.dp)
            ) {
                Text(text = "Get Started", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InitialScreenPreview() {
    ScoutAITheme {
        InitialScreen(onGetStartedClick = {})
    }
}