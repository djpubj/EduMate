package com.test.begin2.ui.components.classwork_component

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.dataclass.Assignment
import com.test.begin2.ui.presentation.classwork.ClassworkViewmodel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentScreen(navController: NavController, context: Context, classId: String?) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }

    val viewmodel: ClassworkViewmodel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    Button(
                        onClick = {
                            // Validate fields before posting
                            titleError = title.isBlank()
                            descriptionError = description.isBlank()
                            dateError = date.isBlank()

                            if (!titleError && !descriptionError && !dateError) {
                                if (classId != null) {
                                    viewmodel.createassignment(
                                        classId = classId,
                                        assignment = Assignment(
                                            title = title,
                                            description = description,
                                            dueDate = date
                                        )
                                    )
                                    Log.e("Assignment", "Created successfully")
                                    navController.navigateUp()
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 15.dp),
                        enabled = title.isNotBlank() && description.isNotBlank() && date.isNotBlank() // Disable if fields are empty
                    ) {
                        Text(text = "Assign")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
        ) {
            // Assignment Title TextField
            TextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = it.isBlank()
                },
                label = { Text(text = "Assignment title (Required)") },
                modifier = Modifier.fillMaxWidth(),
                isError = titleError,
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.5f),
                )
            )
            if (titleError) {
                Text(text = "Title is required!", color = Color.Red, fontSize = 12.sp)
            }

            HorizontalDivider()

            // Description TextField
            TextField(
                value = description,
                onValueChange = {
                    description = it
                    descriptionError = it.isBlank()
                },
                label = { Text(text = "Description (Required)") },
                modifier = Modifier.fillMaxWidth(),
                isError = descriptionError,
                leadingIcon = { Icon(Icons.Filled.Menu, contentDescription = null) },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.5f),
                )
            )
            if (descriptionError) {
                Text(text = "Description is required!", color = Color.Red, fontSize = 12.sp)
            }

            HorizontalDivider()
            DatePickerDialog(onSuccess = { selectedDate ->
                date = selectedDate
                dateError = selectedDate.isBlank()
            })
            if (dateError) {
                Text(text = "Due Date is required!", color = Color.Red, fontSize = 12.sp)
            }

            HorizontalDivider()
        }
    }
}

