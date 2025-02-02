package com.test.begin2.ui.presentation.home

import android.R
import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.dataclass.AuthState
import com.test.begin2.dataclass.ClassRoom
import com.test.begin2.dataclass.Role
import com.test.begin2.dataclass.User
import com.test.begin2.navigation.create
import com.test.begin2.navigation.join
import com.test.begin2.navigation.login
import com.test.begin2.ui.components.dialogbox.DialogBox
import com.test.begin2.ui.components.dialogbox.ProfileDialogBox
import com.test.begin2.ui.components.dialogbox.UpdateUserNameBox
import com.test.begin2.ui.components.home_components.AddButtonBottomSheet
import com.test.begin2.ui.components.home_components.Home_Card
import com.test.begin2.ui.presentation.NothingPresent
import com.test.begin2.ui.presentation.login_signin.AuthViewmodel
import com.test.begin2.ui.theme.GreenGrey80
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home_Screen(navController: NavController) {

    val authViewmodel: AuthViewmodel = viewModel()
    val authState = authViewmodel.authState.observeAsState()
    val viewModel = viewModel(modelClass = HomeViewModel::class.java)
    val createdclassrooms by viewModel.createdclassrooms
    val enrolledclassrooms by viewModel.enrolledclassrooms
    Log.e("createdclass", "created ${createdclassrooms}")

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(login)
            else -> {
                authViewmodel.fetchUserByUid()
                while (viewModel.user.value == null) {
                    delay(10)
                }
                viewModel.fetchClassrooms()
            }

        }
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheetforadd by remember { mutableStateOf(false) }
    var showDialogforsignout by remember { mutableStateOf(false) }
    var showDialogforprofile by remember { mutableStateOf(false) }
    var updateprofile by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isRefreshing by remember { mutableStateOf(false) }
    BackHandler {
        (context as? Activity)?.finish()
    }
    val refreshstate = rememberPullToRefreshState()

    val list1 = listOf(
        BottomSheetItem(title = "Join", onclick = {
            showBottomSheetforadd = false
            navController.navigate(join)
        }),
        BottomSheetItem("Create", onclick = {
            showBottomSheetforadd = false
            navController.navigate(create)
        })
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(color = GreenGrey80),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "EduMate",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                HorizontalDivider()
                NavigationDrawerItem(label = { Text(text = "Home") },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                    }
                )
                HorizontalDivider()
                NavigationDrawerItem(label = { Text(text = "SignOut") },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "SignOut"
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            showDialogforsignout = true
                        }

                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.shadow(8.dp),
                    title = { Text("EduMate") },
                    navigationIcon = {
                        IconButton(onClick =
                        {
                            coroutineScope.launch { drawerState.open() }
                        }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        // Left Icon
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu_gallery), // Replace with real icon
                            contentDescription = null,
                            modifier = Modifier
                                .size(33.dp)
                                .background(Color.LightGray, RoundedCornerShape(24.dp))
                                .clickable {
                                    coroutineScope.launch {
                                        showDialogforprofile = true
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(GreenGrey80),
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        showBottomSheetforadd = true
                    }
                }) {
                    Text("+", fontSize = 24.sp, color = Color.White)
                }
            },
            content = { paddingValues ->
                if (createdclassrooms == emptyList<ClassRoom>() && enrolledclassrooms == emptyList<ClassRoom>()) {
                    NothingPresent(text = "classrooms")
                }
                PullToRefreshBox(
                    state = refreshstate,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        coroutineScope.launch {
                            isRefreshing = true
                            viewModel.fetchClassrooms()
                            delay(2000)
                            isRefreshing = false
                        }
                    },
                    indicator = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 65.dp) // Adjust where indicator appears
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
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {
                        items(createdclassrooms) { item ->
                            Home_Card(
                                classRoom = item,
                                navController = navController,
                                role = Role.TEACHER
                            )
                        }
                        items(enrolledclassrooms) { item ->
                            Home_Card(
                                classRoom = item,
                                navController = navController,
                                role = Role.STUDENT
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp)) // Adjust height as needed
                        }
                    }
                }
            }
        )
        if (showBottomSheetforadd) {
            AddButtonBottomSheet(
                BottomSheetItemList = list1,
                onDismiss = { showBottomSheetforadd = false })
        }
        if (showDialogforsignout) {
            DialogBox(
                onclick = {
                    coroutineScope.launch {
                        authViewmodel.signOut()
                    }
                },
                onDismiss = { showDialogforsignout = false },
                buttonspell = "Signout"
            )
        }
        if (updateprofile) {
            viewModel.user.value?.let {
                UpdateUserNameBox(onDismiss = { updateprofile = false }, user = it)
            }
        }
        if (showDialogforprofile) {
            viewModel.user.value?.let {
                ProfileDialogBox(onDismiss = {
                    showDialogforprofile = false
                }, user = it, onClick = { updateprofile = true })
            }
        }
    }
}
