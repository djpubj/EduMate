package com.test.begin2.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.test.begin2.ui.components.classwork_component.AssignmentManagementUI
import com.test.begin2.ui.components.classwork_component.AssignmentScreen
import com.test.begin2.ui.components.classwork_component.AssignmentSubmissionUI
import com.test.begin2.ui.components.classwork_component.ShowDescriptionUI
import com.test.begin2.ui.components.people_component.StudentAddButton
import com.test.begin2.ui.components.people_component.TeacherAddButton
import com.test.begin2.ui.components.stream_component.AnnouncementScreen
import com.test.begin2.ui.presentation.create_class.Create
import com.test.begin2.ui.presentation.home.Home_Screen
import com.test.begin2.ui.presentation.join_class.Join
import com.test.begin2.ui.presentation.login_signin.Login
import com.test.begin2.ui.presentation.login_signin.SignUp

@Composable
fun PageNavigation(context: Context) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = login, builder = {

        composable<home> {
            Home_Screen(navController = navController)
        }
        composable<bottomnavigation> {
            val args = it.toRoute<bottomnavigation>()
            BottomNavigation(
                navController = navController,
                classId = args.classId,
                classname = args.classname,
                role = args.role
            )
        }
        composable<assignment> {
            val args = it.toRoute<assignment>()
            AssignmentScreen(
                navController = navController,
                context = context,
                classId = args.classId
            )
        }
        composable<announcement> {
            val args = it.toRoute<announcement>()
            AnnouncementScreen(navController = navController, classId = args.classId)
        }
        composable<join> {
            Join(navController = navController)
        }
        composable<create> {
            Create(navController = navController)
        }
        composable<studentadd> {
            val args = it.toRoute<studentadd>()
            StudentAddButton(navController = navController, classId = args.classId)
        }
        composable<teacheradd> {
            TeacherAddButton(navController = navController)
        }
        composable<assignmentsubmissionui> {
            val args = it.toRoute<assignmentsubmissionui>()
            AssignmentSubmissionUI(
                navController = navController,
                classId = args.classId,
                assignmentId = args.assignmentId,
                title = args.title,
                decription = args.decription,
                duedate = args.duedate
            )
        }
        composable<assignmentmanagementui> {
            val args = it.toRoute<assignmentmanagementui>()
            AssignmentManagementUI(
                navController = navController,
                classId = args.classId,
                assignmentId = args.assignmentId
            )
        }
        composable<login> {
            Login(navController = navController)
        }
        composable<signup> {
            SignUp(navController = navController)
        }

        composable<showdescription> {
            val args = it.toRoute<showdescription>()
            ShowDescriptionUI(
                navController = navController,
                studentname = args.studentname,
                description =args.description
            )
        }
    })
}