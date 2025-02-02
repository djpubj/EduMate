package com.test.begin2.ui.components.stream_component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.dataclass.Announcement
import com.test.begin2.ui.presentation.stream.StreamViewmodel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementScreen(navController: NavController, classId: String?) {
    val viewmodel: StreamViewmodel = viewModel()

    var announcementTitle by remember { mutableStateOf("") }
    var announcementDescription by remember { mutableStateOf("") }
    var announcementTitleError by remember { mutableStateOf(false) }
    var announcementDescriptionError by remember { mutableStateOf(false) }

    val currentDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            // Validate fields before posting
                            announcementTitleError = announcementTitle.isBlank()
                            announcementDescriptionError = announcementDescription.isBlank()

                            if (!announcementTitleError && !announcementDescriptionError) {
                                if (classId != null) {
                                    viewmodel.addAnnouncement(
                                        classId = classId,
                                        announcement = Announcement(
                                            announcementTitle = announcementTitle,
                                            message = announcementDescription,
                                            date = currentDate
                                        )
                                    )
                                }
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.padding(end = 15.dp),
                        enabled = announcementTitle.isNotBlank() && announcementDescription.isNotBlank() // Disable if fields are empty
                    ) {
                        Text(text = "Post")
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
        ) {
            // Announcement Title TextField
            TextField(
                value = announcementTitle,
                onValueChange = {
                    announcementTitle = it
                    announcementTitleError = it.isBlank()
                },
                label = {
                    Text(
                        text = "Announcement Title (Required)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                isError = announcementTitleError,
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
            if (announcementTitleError) {
                Text(
                    text = "Title is required!",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
            HorizontalDivider()

            // Announcement Description TextField
            TextField(
                value = announcementDescription,
                onValueChange = {
                    announcementDescription = it
                    announcementDescriptionError = it.isBlank()
                },
                label = { Text(text = "Announce something to your class (Required)") },
                modifier = Modifier.fillMaxWidth(),
                isError = announcementDescriptionError,
                leadingIcon = { Icon(Icons.Filled.Menu, contentDescription = null) },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
            if (announcementDescriptionError) {
                Text(
                    text = "Description is required!",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
            HorizontalDivider()
        }
    }
}
