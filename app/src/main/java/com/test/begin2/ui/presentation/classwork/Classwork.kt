package com.test.begin2.ui.presentation.classwork

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.dataclass.Assignment
import com.test.begin2.dataclass.Role
import com.test.begin2.dataclass.User
import com.test.begin2.navigation.assignment
import com.test.begin2.navigation.assignmentmanagementui
import com.test.begin2.navigation.assignmentsubmissionui
import com.test.begin2.ui.components.dialogbox.DialogBox
import com.test.begin2.ui.presentation.NothingPresent
import com.test.begin2.ui.theme.LightGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Classwork(navController: NavController, role: Role, classId: String?) {
    val viewmodel: ClassworkViewmodel = viewModel()
    val AllAssignment by viewmodel.AllAssignment
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshstate = rememberPullToRefreshState()

    LaunchedEffect(classId) {
        coroutineScope.launch {
            if (classId != null) {
                viewmodel.getAssignment(classId = classId)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            if (role == Role.TEACHER) {
                FloatingActionButton(
                    onClick = { navController.navigate(assignment(classId = classId)) },
                    modifier = Modifier.padding(bottom = 25.dp, end = 5.dp)
                ) {
                    Text("+", fontSize = 24.sp, color = Color.White)
                }
            }
        },
    ) { padding ->
        if (AllAssignment == emptyList<Assignment>()) {
            NothingPresent(text = "assignments")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 10.dp, bottom = 30.dp)
        ) {
            PullToRefreshBox(
                state = refreshstate,
                isRefreshing = isRefreshing,
                onRefresh = {
                    coroutineScope.launch {
                        isRefreshing = true
                        if (classId != null) {
                            viewmodel.getAssignment(classId = classId)
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
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(AllAssignment) { item ->
                        ClassAssignment(
                            navController = navController,
                            role = role,
                            assignment = item,
                            classId = classId
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
fun ClassAssignment(
    classId: String?,
    navController: NavController,
    role: Role,
    assignment: Assignment
) {
    var showDialog by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    val viewmodel: ClassworkViewmodel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                coroutineScope.launch {
                    if (role == Role.TEACHER) {
                        navController.navigate(
                            assignmentmanagementui(
                                classId = classId,
                                assignmentId = assignment.assignmentId
                            )
                        )
                    } else {
                        navController.navigate(
                            assignmentsubmissionui(
                                classId = classId, assignmentId = assignment.assignmentId,
                                title = assignment.title,
                                decription = assignment.description,
                                duedate = assignment.dueDate
                            )
                        )
                    }
                }
            },
        shape = RoundedCornerShape(8.dp),
        color = LightGray
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = assignment.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (assignment.dueDate.isNotEmpty()) {
                    Text(
                        text = assignment.dueDate,
                        fontSize = 12.sp,
                    )
                }
            }
            if (role == Role.TEACHER) {
                IconButton(onClick = { coroutineScope.launch { isExpanded = !isExpanded } }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                    )
                }
            }
            // Dropdown Menu
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
                    text = { Text(text = "Delete") }
                )
            }
            if (showDialog) {
                DialogBox(
                    onclick = {
                        coroutineScope.launch {
                            if (classId != null) {
                                viewmodel.removeAssignment(
                                    classId = classId,
                                    assignmentId = assignment.assignmentId
                                )
                            }
                        }
                    },
                    onDismiss = { showDialog = false },
                    buttonspell = "Delete"
                )
            }
        }
    }
}
