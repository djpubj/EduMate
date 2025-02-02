package com.test.begin2.ui.presentation.stream

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.dataclass.Role
import com.test.begin2.dataclass.User
import com.test.begin2.ui.components.stream_component.AnnouncementTakePlace
import com.test.begin2.ui.components.stream_component.Announcementtab
import com.test.begin2.ui.presentation.NothingPresent
import com.test.begin2.ui.theme.GreenGrey80
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Stream(navController: NavController, classId: String?, role: Role) {
    val viewmodel: StreamViewmodel = viewModel()
    val announcements by viewmodel.announcements
    val classroom by viewmodel.classroom
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshstate = rememberPullToRefreshState()
    LaunchedEffect(classId) {
        if (classId != null) {
            viewmodel.getAnnouncements(classId)
        }
    }

    Scaffold { padding ->
        if (announcements == emptyList<User>()) {
            NothingPresent(text = "announcements")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = 30.dp)
        ) {
            PullToRefreshBox(
                state = refreshstate,
                isRefreshing = isRefreshing,
                onRefresh = {
                    coroutineScope.launch {
                        isRefreshing = true
                        if (classId != null) {
                            viewmodel.getAnnouncements(classId)
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
                Column {
                    Box(
                        modifier = Modifier
                            .shadow(8.dp)
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(GreenGrey80), contentAlignment = Alignment.BottomStart
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "${classroom.className}",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(text = "${classroom.batch}", fontSize = 19.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        item {
                            Announcementtab(
                                title = "Announce something to  your class",
                                navController = navController,
                                classId = classId
                            )
                        }

                        items(announcements.reversed()) { item ->
                            AnnouncementTakePlace(
                                title = item.announcementTitle,
                                description = item.message,
                                date = item.date,
                                role = role,
                                onClick = {
                                    if (classId != null) {
                                        viewmodel.deleteAnnouncement(
                                            classId = classId,
                                            announcementId = item.announcementId
                                        )
                                        Log.e("heloo", "annoncement")
                                    }
                                }
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
}

