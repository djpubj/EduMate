package com.test.begin2.ui.components.classwork_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.navigation.showdescription
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentManagementUI(
    navController: NavController,
    classId: String?,
    assignmentId: String?
) {
    val assignmetviewmodel: AssignmentManagementViewmodel = viewModel()
    val Allsubmission by assignmetviewmodel.AllSubmissions
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(classId) {
        coroutineScope.launch {
            if (classId != null) {
                if (assignmentId != null) {
                    assignmetviewmodel.getAssignmentSubmissions(
                        classId = classId,
                        assignmentId = assignmentId
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "")
            }, navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Menu"
                    )
                }
            }, modifier = Modifier.shadow(8.dp)
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Student work", fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp, color = Color.Gray
            )

            // Handed In and Assigned Stats
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = Allsubmission.size.toString() + " Student Handed in",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Handed In Section

            Text(
                text = "Handed In",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
            LazyColumn {
                items(Allsubmission) { item ->
                    StudentItem(studentName = item.username, status = "Handed in",
                        onClick = {
                            navController.navigate(
                                showdescription(
                                    studentname = item.username,
                                    description = item.submitedAssignment
                                )
                            )
                        })
                }
            }
        }
    }
}

@Composable
fun StudentItem(studentName: String, status: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Student Icon
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Student Icon",
            modifier = Modifier
                .size(40.dp)
                .padding(end = 8.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        // Student Name and Status
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = studentName, fontSize = 16.sp, fontWeight = FontWeight.Normal
            )
        }

        Text(
            text = status,
            fontSize = 16.sp,
            color = if (status == "Handed in") Color.Green else Color.Gray
        )
    }
}