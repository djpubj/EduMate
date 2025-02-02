package com.test.begin2.ui.presentation.login_signin

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.test.begin2.dataclass.AuthState
import com.test.begin2.navigation.home
import com.test.begin2.navigation.signup
import com.test.begin2.ui.presentation.startinginterface.StartingUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun Login(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()
    val authViewmodel: AuthViewmodel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = authViewmodel.authState.observeAsState()
    var showUI by remember { mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler {
        (context as? android.app.Activity)?.finish()
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                coroutineScope.launch {
                    delay(1000L)
                    navController.navigate(home)
                }
            }

            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> {
                delay(1000L)
                showUI = true
            }
        }
    }

    if (showUI) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login Page", fontSize = 32.sp)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewmodel.login(email, password)
                },
                enabled = authState.value != AuthState.Loading
            ) {
                Text(text = "Login")
            }


            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = {
                navController.navigate(signup)
            }) {
                Text(text = "Don't have an account, Signup")
            }

        }
    } else {
        StartingUI()
    }
}