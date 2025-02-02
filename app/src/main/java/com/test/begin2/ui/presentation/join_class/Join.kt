package com.test.begin2.ui.presentation.join_class

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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.navigation.home
import com.test.begin2.ui.presentation.home.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Join(navController: NavController) {
    val viewModel = viewModel(modelClass = HomeViewModel::class.java)
    var classcode by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    Button(onClick = {
                        viewModel.addStudentinClassroom(classId = classcode)
//                        viewModel.updateEnrolledClasses(newEnrolledClass = classcode)
                        navController.navigate(home)
                    }, modifier = Modifier.padding(end = 15.dp)) {
                        Text(text = "Join")
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
                        text = "Class code\nAsk your teacher for the class code, then enter it here.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp) // Adds space below the text
                    )

                    OutlinedTextField(
                        value = classcode,
                        onValueChange = { classcode = it },
                        label = {
                            Text(
                                text = "Write Class Code",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        )
                    )
                }
            }

            Text(
                text = buildAnnotatedString {
                    append("To sign in with a class code\n")
                    append("• Use an authorized account\n")
                    append("• Use a class code with 5-7 letters or numbers, and no spaces or symbols")
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }


    }
}