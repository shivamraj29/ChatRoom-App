package com.example.chatroom


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.example.chatroom.ui.theme.PrimaryPink
import com.example.chatroom.ui.theme.PrimaryPinkBlended
import com.example.chatroom.ui.theme.PrimaryPinkDark
import com.example.chatroom.ui.theme.darkTextColor


@Composable
fun createAccount(authViewModel: AuthViewModel, navController: NavController, context: Context) {

    var firstName by remember {
        mutableStateOf("")
    }

    var lastName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }
    var showWarning by remember{
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(
                Brush.verticalGradient(
                    0f to PrimaryPinkBlended,
                    1f to PrimaryPink
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.hello),
            contentDescription = null,
            modifier = Modifier.size(150.dp).fillMaxWidth().align(Alignment.CenterHorizontally),
            alignment = Alignment.Center)
        message(title = "", subTitle = "Lets get started")
        TextField(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(), value = firstName,
            onValueChange = {firstName = it},
            visualTransformation = VisualTransformation.None,
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = darkTextColor,
                unfocusedTextColor = darkTextColor,
                unfocusedPlaceholderColor = darkTextColor,
                focusedPlaceholderColor = darkTextColor,
                focusedLeadingIconColor = darkTextColor,
                unfocusedLeadingIconColor = darkTextColor,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            placeholder = {
                Text(text = "First Name")
            }
        )

        //    Spacer(modifier = Modifier.height(10.dp))

        TextField(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(), value = lastName,
            onValueChange = {lastName = it},
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = darkTextColor,
                unfocusedTextColor = darkTextColor,
                unfocusedPlaceholderColor = darkTextColor,
                focusedPlaceholderColor = darkTextColor,
                focusedLeadingIconColor = darkTextColor,
                unfocusedLeadingIconColor = darkTextColor,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            placeholder = {
                Text(text = "Last Name")
            }
        )
        Spacer(modifier = Modifier.padding(12.dp))
        TextField(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(), value = email,
            onValueChange = {email = it},
            visualTransformation = VisualTransformation.None,
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = darkTextColor,
                unfocusedTextColor = darkTextColor,
                unfocusedPlaceholderColor = darkTextColor,
                focusedPlaceholderColor = darkTextColor,
                focusedLeadingIconColor = darkTextColor,
                unfocusedLeadingIconColor = darkTextColor,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            placeholder = {
                Text(text = "Email")
            }
        )
        Spacer(modifier = Modifier.padding(12.dp))
        TextField(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(), value = password,
            onValueChange = {password = it},
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = darkTextColor,
                unfocusedTextColor = darkTextColor,
                unfocusedPlaceholderColor = darkTextColor,
                focusedPlaceholderColor = darkTextColor,
                focusedLeadingIconColor = darkTextColor,
                unfocusedLeadingIconColor = darkTextColor,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            placeholder = {
                Text(text = "Password")
            }
        )
        Spacer(modifier = Modifier.padding(16.dp))
        ActionButton(text = "Create Account",
            onCliciked = {
                if (email.trim()!= "" && password.trim()!= ""&& firstName.trim()!=""){
                    authViewModel.signup(firstName.trim(),lastName.trim(),email.trim(),password.trim(), navController, context)
                }else{
                    Toast.makeText(context, "Fill all the deatils", Toast.LENGTH_LONG).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryPinkDark,
                contentColor = Color.White
            ), shadowColor = PrimaryPinkDark,
            modifier = Modifier.padding(horizontal = 24.dp))



    }

}


/*

        Text(text = "Create Account", fontSize = 26.sp)
        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(value = firstName, onValueChange = { firstName = it },
            label = { Text(text = "First Name") }, modifier = Modifier.padding(8.dp)
        )

        OutlinedTextField(value = lastName, onValueChange = { lastName = it },
            label = { Text(text = "Last Name") }, modifier = Modifier.padding(8.dp)
        )

        OutlinedTextField(value = email, onValueChange = { email = it },
            label = { Text(text = "Email Id") }, modifier = Modifier.padding(8.dp)
        )

        OutlinedTextField(value = password, onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = "Password") })

        Button(onClick = {if (firstName!= "" && email!="" && password!= "")
                            { authViewModel.signup(firstName.trim(),lastName.trim(),email.trim(),password.trim(), navController, context)}
                        else{
                            showWarning= true
                        }
                         }, modifier = Modifier.padding(16.dp)) {
            Text(text = "Create")
        }

        if(showWarning){
            Text(text = "Fill all the details", fontSize = 20.sp, color = Color.Red)
        }*/