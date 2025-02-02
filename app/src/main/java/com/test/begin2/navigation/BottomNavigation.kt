package com.test.begin2.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.test.begin2.dataclass.Navlist
import com.test.begin2.dataclass.Role
import com.test.begin2.ui.presentation.classwork.Classwork
import com.test.begin2.ui.presentation.people.People
import com.test.begin2.ui.presentation.stream.Stream
import com.test.begin2.ui.theme.GreenGrey80
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    navController: NavController,
    classId: String?,
    classname: String?,
    role: Role
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var selecteditem by rememberSaveable { mutableStateOf(0) }

    BackHandler {
        if (selecteditem != 0) {
            selecteditem = 0 // Moves back to the first tab (Stream)
        } else {
            navController.navigate(home)
        }
    }


    val navItemList = remember {
        mutableStateListOf(
            Navlist("Stream", android.R.drawable.ic_menu_sort_by_size),
            Navlist("Classwork", android.R.drawable.ic_menu_agenda),
            Navlist("People", android.R.drawable.ic_menu_gallery)
        )
    }



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
                        color = Color.Black
                    )
                }
                HorizontalDivider()
                NavigationDrawerItem(label = { Text(text = "Home") },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            navController.navigate(home) {
                                popUpTo(home) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        if (selecteditem != 0) {
                            if (classname != null) {
                                Text(text = classname)
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } },
                        ) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    modifier = Modifier.shadow(8.dp)
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.shadow(
                        elevation = 10.dp,
                        spotColor = Color.Black // Dark shadow color
                    ),
                    containerColor = GreenGrey80,
                    contentColor = Color.White
                ) {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selecteditem == index,
                            onClick = {
                                selecteditem = index
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = navItem.icon),
                                    contentDescription = navItem.title,
                                    tint = Color.White
                                )
                            },
                            label = { Text(text = navItem.title, color = Color.White) }
                        )
                    }
                }
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(
                            top = 64.dp,
                            bottom = 60.dp,
                        ) // Adjust left padding to accommodate for a left-side space
                        .fillMaxSize()
                ) {
                    NavScreen(
                        modifier = Modifier.padding(paddingValues),
                        selecteditem = selecteditem,
                        navController = navController,
                        classId = classId,
                        role = role
                    )
                }
            }
        )
    }
}

@Composable
fun NavScreen(
    modifier: Modifier,
    selecteditem: Int,
    navController: NavController,
    classId: String?,
    role: Role
) {
    when (selecteditem) {
        0 -> Stream(navController = navController, classId = classId, role = role)
        1 -> Classwork(navController = navController, classId = classId, role = role)
        2 -> People(navController = navController, classId = classId, role = role)
    }
}