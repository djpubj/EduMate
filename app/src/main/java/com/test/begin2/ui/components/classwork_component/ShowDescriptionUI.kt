package com.test.begin2.ui.components.classwork_component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDescriptionUI(navController: NavController, studentname: String, description: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() })
                    {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Menu"
                        )
                    }
                },
                modifier = Modifier.shadow(8.dp)
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ) // Grey background with rounded corners
                    .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)) // Border around the box
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // Padding for inner content
                ) {
                    Text(
                        text = "Student Name : ${studentname}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp) // Adds space below the text
                    )
                    Box(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .border(
                                2.dp,
                                Color.Black,
                                shape = RoundedCornerShape(8.dp)
                            ) // Border with rounded corners
                            .padding(8.dp) // Padding inside the border
                    ) {
                        Column {
                            Text(
                                text = "Submission by Student ->",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 1.dp,
                                color = Color.Gray
                            )
                            Text(
                                text = "${description}.",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                }
            }

        }


    }
}
