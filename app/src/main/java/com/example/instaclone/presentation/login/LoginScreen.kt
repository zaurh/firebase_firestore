package com.example.instaclone.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instaclone.DestinationScreen
import com.example.instaclone.common.CheckSignedIn
import com.example.instaclone.common.CircularProgressSpinner
import com.example.instaclone.common.navigateTo
import com.example.instaclone.presentation.MainViewModel

@Composable
fun LoginScreen(
    navController: NavController, viewModel: MainViewModel
) {
    CheckSignedIn(navController = navController, viewModel = viewModel)
    val focus = LocalFocusManager.current
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState()
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val emailState = remember { mutableStateOf("zaur@gmail.com") }
            val passwordState = remember { mutableStateOf("zaur123") }

            Image(
                painter = painterResource(id = com.example.instaclone.R.drawable.ig_logo),
                contentDescription = "",
                modifier = Modifier
                    .width(250.dp)
                    .padding(top = 16.dp)
                    .padding(
                        8.dp
                    )
            )
            Text(
                text = "Login",
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif
            )

            OutlinedTextField(value = emailState.value,
                onValueChange = { emailState.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Email") })

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    focus.clearFocus(force = true)
                    viewModel.onLogin(emailState.value, passwordState.value)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Sign In")
            }

            Text(text = "Go to SignUp", color = Color.Blue, modifier = Modifier
                .padding(8.dp)
                .clickable {
                    navigateTo(navController, DestinationScreen.SignUp)
                })

        }

        val isLoading = viewModel.inProgress.value
        if (isLoading) {
            CircularProgressSpinner()
        }
    }

}