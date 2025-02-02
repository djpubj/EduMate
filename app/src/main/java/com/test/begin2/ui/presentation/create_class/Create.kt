package com.test.begin2.ui.presentation.create_class

import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.dataclass.ClassRoom
import com.test.begin2.ui.presentation.home.HomeViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Create(navController: NavController) {
    var classname by remember { mutableStateOf("") }
    var teachername by remember { mutableStateOf("") }
    var batch by remember { mutableStateOf("") }

    var classnameError by remember { mutableStateOf(false) }
    var teachernameError by remember { mutableStateOf(false) }
    var batchError by remember { mutableStateOf(false) }

    val viewModel = viewModel(modelClass = HomeViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current // Get the context here

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
                    Button(
                        onClick = {
                            classnameError = classname.isBlank()
                            teachernameError = teachername.isBlank()
                            batchError = batch.isBlank()

                            if (!classnameError && !teachernameError && !batchError) {
                                coroutineScope.launch {
                                    val classroom = ClassRoom(
                                        className = classname,
                                        teachername = teachername,
                                        batch = batch
                                    )
                                    viewModel.addClassroom(classroom)
                                    viewModel.fetchClassrooms()
                                    navController.navigate(home)
                                    Toast.makeText(
                                        context,
                                        "Classroom ${classroom.className} created",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 15.dp),
                        enabled = classname.isNotBlank() && teachername.isNotBlank() && batch.isNotBlank() // Disable if fields are empty
                    ) {
                        Text(text = "Create")
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
            // Classroom Name TextField
            TextField(
                value = classname,
                onValueChange = {
                    classname = it
                    classnameError = it.isBlank()
                },
                label = { Text(text = "Classroom Name (Required)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                isError = classnameError
            )
            if (classnameError) {
                Text(
                    text = "Classroom name is required!",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
            HorizontalDivider()

            // Batch TextField
            TextField(
                value = batch,
                onValueChange = {
                    batch = it
                    batchError = it.isBlank()
                },
                label = { Text(text = "Batch (Required)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Menu, contentDescription = null) },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                isError = batchError
            )
            if (batchError) {
                Text(
                    text = "Batch is required!",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
            HorizontalDivider()

            // Teacher's Name TextField
            TextField(
                value = teachername,
                onValueChange = {
                    teachername = it
                    teachernameError = it.isBlank()
                },
                label = { Text(text = "Teacher's Name (Required)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Menu, contentDescription = null) },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                isError = teachernameError
            )
            if (teachernameError) {
                Text(
                    text = "Teacher's name is required!",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
            HorizontalDivider()
        }
    }
}
