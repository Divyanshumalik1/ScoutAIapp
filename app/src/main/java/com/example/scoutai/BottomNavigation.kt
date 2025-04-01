package com.example.scoutai

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.colorResource

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.Scoutgreen),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        BottomNavigationItem(
            icon = { Text("Camera") },
            selected = false,
            onClick = { navController.navigate("camera") }
        )
        BottomNavigationItem(
            icon = { Text("Data") },
            selected = false,
            onClick = { navController.navigate("second") }
        )
    }
}