package com.test.begin2.ui.components.stream_component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.begin2.dataclass.Role
import com.test.begin2.ui.components.dialogbox.DialogBox
import com.test.begin2.ui.theme.LightGray
import kotlinx.coroutines.launch


@Composable
fun AnnouncementTakePlace(
    title: String,
    description: String = "",
    date: String = "",
    onClick: () -> Unit,
    role: Role
) {
    var showDialog by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = LightGray
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (role == Role.TEACHER) {
                    // Custom Icon for Dropdown
                    IconButton(onClick = { coroutineScope.launch { isExpanded = !isExpanded } }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options"
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
            }

            if (description.isNotEmpty()) {
                Text(text = description)
            }

            if (date.isNotEmpty()) {
                Text(
                    text = date,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            if (showDialog) {
                DialogBox(
                    onclick = { onClick() },
                    onDismiss = { showDialog = false },
                    buttonspell = "Delete"
                )
            }
        }

    }
}
