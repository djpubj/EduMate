package com.test.begin2.ui.presentation.startinginterface

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.begin2.R


@Composable
fun StartingUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // Align the children horizontally in the center
    ) {
        // Image centered at the top
        Image(
            painter = painterResource(id = R.drawable.startingimage2), // Replace with your image resource
            contentDescription = "Start Screen",
            modifier = Modifier.fillMaxWidth() // Adjust width of the image
        )

        Spacer(modifier = Modifier.height(16.dp)) // Space between image and text

        // Text centered in the Box
        Text(
            text = "Welcome To EduMate",
            textAlign = TextAlign.Center, // Center-align the text horizontally
            fontSize = 32.sp, // Set the font size for heading style
            fontWeight = FontWeight.Bold, // Make the text bold
            color = Color(0xFF000080), // Navy blue color
            modifier = Modifier.fillMaxWidth() // Ensure the text spans the full width
        )
    }
}
