package com.test.begin2.ui.components.classwork_component

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.dataclass.Assignmentsubmited
import com.test.begin2.ui.presentation.classwork.ClassworkViewmodel
import com.test.begin2.ui.presentation.login_signin.AuthViewmodel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentSubmissionUI(
    navController: NavController,
    classId: String?,
    assignmentId: String,
    title: String,
    decription: String,
    duedate: String
) {
    val coroutineScope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    val viewmodel: ClassworkViewmodel = viewModel()
    val authViewmodel: AuthViewmodel = viewModel()

    authViewmodel.fetchUserByUid()
    val user = authViewmodel.fetchuser


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "")
                },
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
                            coroutineScope.launch {
                                if (classId != null) {
                                    user.value?.let {
                                        Assignmentsubmited(
                                            username = user.value!!.username,
                                            useruid = it.useruid,
                                            submitedAssignment = text
                                        )
                                    }?.let {
                                        viewmodel.submitassignment(
                                            classId = classId,
                                            assignmentId = assignmentId,
                                            assignmentsubmited = it
                                        )
                                    }
                                }
                                navController.popBackStack()
                            }
                        }, modifier = Modifier.padding(end = 15.dp)
                    ) {
                        Text(text = "Submit")
                    }
                },
                modifier = Modifier.shadow(8.dp)
            )
        },
    ) { PaddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.3f))
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PaddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "${duedate}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Work cannot be considered submitted after the due date.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider()

                    Text(
                        text = decription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    HorizontalDivider()
                    Text(
                        text = "Write Assignment below",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        maxLines = Int.MAX_VALUE, // Expands as text increases
                        placeholder = { Text("Enter text...") }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
