package com.test.begin2.ui.components.home_components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.dataclass.ClassRoom
import com.test.begin2.dataclass.Role
import com.test.begin2.navigation.bottomnavigation
import com.test.begin2.ui.components.dialogbox.DialogBox
import com.test.begin2.ui.presentation.home.BottomSheetItem
import com.test.begin2.ui.presentation.home.HomeViewModel
import kotlinx.coroutines.launch


@Composable
fun Home_Card(
    classRoom: ClassRoom,
    navController: NavController,
    role: Role
) {
    var showDialogfordelete by remember { mutableStateOf(false) }
    var showDialogforunenroll by remember { mutableStateOf(false) }
    var showBottomSheetfordot by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val viewModel = viewModel(modelClass = HomeViewModel::class.java)
    val context = LocalContext.current // Get the context here

    val list2 = listOf(
        BottomSheetItem(title = "Delete", onclick = {
            coroutineScope.launch {
                showBottomSheetfordot = false
                showDialogfordelete = true
            }
        }),
        BottomSheetItem(title = "Unenroll", onclick = {
            coroutineScope.launch {
                showBottomSheetfordot = false
                showDialogforunenroll = true
            }
        })
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp)
            .clickable {
                navController.navigate(
                    bottomnavigation(
                        classId = classRoom.classId,
                        classname = classRoom.className,
                        role = role
                    )
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Icon
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Replace with real icon
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.LightGray, RoundedCornerShape(24.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Title and Subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(text = classRoom.className, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = classRoom.batch, fontSize = 14.sp, color = Color.Gray)
            }

            // Extra Info
            if (role == Role.STUDENT) {
                Text(text = classRoom.teachername, fontSize = 14.sp, color = Color.Gray)
            }
            if (role ==  Role.TEACHER) {
                var size = classRoom.students.size
                if (size == 1) {
                    Text(
                        text = classRoom.students.size.toString() + " Student",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                } else {
                    Text(
                        text = classRoom.students.size.toString() + " Students",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

            }
            IconButton(
                onClick = {
                    showBottomSheetfordot = true
                }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
        }
    }
    if (showBottomSheetfordot) {
        PresentBottomSheet(
            BottomSheetItemList = list2,
            onDismiss = { showBottomSheetfordot = false },
            role = role
        )
    }
    if (showDialogfordelete) {
        DialogBox(
            onclick = {
                coroutineScope.launch {
                    viewModel.deleteClassroom(classRoom.classId)
                    viewModel.fetchClassrooms()
                    Toast.makeText(
                        context,
                        "Classroom " + classRoom.className + " deleted ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onDismiss = { showDialogfordelete = false },
            buttonspell = "Delete"
        )
    }
    if (showDialogforunenroll) {
        DialogBox(
            onclick = {
                coroutineScope.launch {
                    viewModel.unenrollStudent(classId = classRoom.classId)
                    viewModel.fetchClassrooms()
                    Toast.makeText(
                        context,
                        "Classroom " + classRoom.className + " Unrolled ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onDismiss = { showDialogfordelete = false },
            buttonspell = "Unenroll"
        )
    }
}
