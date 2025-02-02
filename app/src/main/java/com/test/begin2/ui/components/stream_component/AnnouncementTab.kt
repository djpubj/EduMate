package com.test.begin2.ui.components.stream_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.test.begin2.navigation.announcement
import com.test.begin2.ui.theme.LightGray

@Composable
fun Announcementtab(
    title: String, icon: ImageVector? = null, navController: NavController, classId: String?
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(announcement(classId = classId)) },
        color = LightGray
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Replace with real icon
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, RoundedCornerShape(24.dp))
            )

            Text(
                text = title, modifier = Modifier.padding(start = 15.dp)
            )
        }
    }
}
