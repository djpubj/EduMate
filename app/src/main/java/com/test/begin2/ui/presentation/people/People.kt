package com.test.begin2.ui.presentation.people

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.dataclass.Role
import com.test.begin2.dataclass.User
import com.test.begin2.navigation.studentadd
import com.test.begin2.navigation.teacheradd
import com.test.begin2.ui.components.dialogbox.DialogBox
import com.test.begin2.ui.presentation.NothingPresent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun People(navController: NavController, classId: String?, role: Role) {

    val viewModel = viewModel(modelClass = PeopleViewmodel::class.java)
    val AllStudents by viewModel.allStudents
    val AllTeachers by viewModel.allTeachers
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshstate = rememberPullToRefreshState()

    LaunchedEffect(classId) {
        coroutineScope.launch {
            if (classId != null) {
                viewModel.getstudents(classId = classId)
                viewModel.getteachers(classId = classId)
            }
        }
    }

    Scaffold(
    ) { padding ->
        if (AllStudents == emptyList<User>()) {
            NothingPresent(text = "students")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = 20.dp)
        ) {
            PullToRefreshBox(
                state = refreshstate,
                isRefreshing = isRefreshing,
                onRefresh = {
                    coroutineScope.launch {
                        isRefreshing = true
                        if (classId != null) {
                            viewModel.getstudents(classId = classId)
                            viewModel.getteachers(classId = classId)
                        }
                        delay(2000)
                        isRefreshing = false
                    }
                },
                indicator = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Indicator(
                            modifier = Modifier.align(Alignment.TopCenter), // Adjust indicator position
                            isRefreshing = isRefreshing,
                            state = refreshstate
                        )
                    }
                }
            ) {
                LazyColumn {
                    item {
                        // Teachers Section
                        TopicTitle(
                            "Teachers", onclick = { navController.navigate(teacheradd) },
                            forwhat = false
                        )
                    }
                    items(AllTeachers) { teacher ->
                        TeachersAndStudent(
                            classId = classId,
                            user = teacher,
                            role = role,
                            forwhat = false
                        )
                    }
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Students Section
                        TopicTitle(
                            "Students",
                            onclick = { navController.navigate(studentadd(classId = classId)) },
                            forwhat = true
                        )
                    }
                    items(AllStudents) { student ->
                        TeachersAndStudent(
                            classId = classId,
                            user = student,
                            role = role,
                            forwhat = true
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp)) // Adjust height as needed
                    }
                }
            }
        }
    }
}


@Composable
fun TopicTitle(title: String, onclick: () -> Unit, forwhat: Boolean) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        )
        if (forwhat == true) {
            IconButton(onClick = { onclick() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Invite Icon",
                    modifier = Modifier
                        .size(45.dp)
                )
            }
        }
    }
}

@Composable
fun TeachersAndStudent(classId: String?, user: User, role: Role, forwhat: Boolean) {
    val viewModel = viewModel(modelClass = PeopleViewmodel::class.java)
    var isExpanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Teacher Icon",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            user.username,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (role == Role.TEACHER && forwhat == true) {
            IconButton(onClick = { coroutineScope.launch { isExpanded = !isExpanded } }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options",
                )
            }
        }
        if (showDialog) {
            DialogBox(onclick = {
                coroutineScope.launch {
                    if (classId != null) {
                        viewModel.removestudent(classId = classId, studentId = user.useruid)
                        delay(100)
                        viewModel.getstudents(classId = classId)
                    }
                }
            }, onDismiss = { showDialog = false }, buttonspell = "Remove")
        }
        DropdownMenu(
            modifier = Modifier,
            expanded = isExpanded,
            onDismissRequest = { coroutineScope.launch { isExpanded = false } },
            offset = DpOffset(
                x = 200.dp,
                y = 0.dp
            ) // Adjust this value to position at the bottom
        ) {
            DropdownMenuItem(
                onClick = {
                    coroutineScope.launch {
                        isExpanded = false
                        showDialog = true
                    }
                },
                text = { Text(text = "Remove") }
            )
        }
    }
}