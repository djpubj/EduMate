package com.test.begin2.ui.components.dialogbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.begin2.dataclass.User
import com.test.begin2.ui.presentation.login_signin.AuthViewmodel


@Composable
fun UpdateUserNameBox(
    onDismiss: () -> Unit, user: User
) {
    var newusername by remember { mutableStateOf(user.username) }
    val authViewmodel: AuthViewmodel = viewModel()
    Dialog(onDismissRequest = {
        onDismiss()
    }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium, tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = newusername,
                            onValueChange = { newusername = it },
                            label = { Text(text = "Update User Name") }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            onDismiss()
                            user.username = newusername
                            authViewmodel.updateUser(user = user)
                        },
                    ) {
                        Text("Update")
                    }
                }
            }
        }
    }
}
