package com.example.chatroom

import android.content.Context
import android.widget.Toast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp

import androidx.navigation.NavController

import com.example.chatroom.ui.theme.PrimaryPink
import com.example.chatroom.ui.theme.PrimaryPinkBlended
import com.example.chatroom.ui.theme.PrimaryPinkDark
import com.example.chatroom.ui.theme.PrimaryPinkLight
import com.example.chatroom.ui.theme.darkTextColor

@Composable
fun logInScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    context: Context
){


    var isLoading = authViewModel.isLoading.collectAsState(initial = true)
    val user = authViewModel.getUser.collectAsState(initial = dataToCache(email = "", password = "", profPic = drawableToByteArray(context)))


    if (isLoading.value){
        CircularProgressIndicator()

    }else {
       if(user.value?.email == null){
            loginSce(
                authViewModel = authViewModel,
                context = context,
                navController = navController
            )

        }else{
            if (user.value.email.toString() != ""){
            authViewModel.login(user.value.email, user.value.password,context , navController)}

        }
    }

}

@Composable
fun loginSce(authViewModel: AuthViewModel, context: Context, navController: NavController){
    var userEmail by remember {
        mutableStateOf("")
    }

    var userPass by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .background(
            Brush.verticalGradient(
                0f to PrimaryPinkBlended,
                1f to PrimaryPink
            )
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),

        ) {
            Image(painter = painterResource(R.drawable.clipart),
                contentDescription = null,
                modifier = Modifier.size(300.dp).fillMaxWidth().align(Alignment.CenterHorizontally),
                alignment = Alignment.Center)
            message(title = "Welcome to", subTitle = "Chatroom" )

            TextField(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(), value = userEmail,
                onValueChange = {userEmail = it},
                visualTransformation = VisualTransformation.None,
                singleLine = true,
                shape = RoundedCornerShape(50 ),
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
                    unfocusedContainerColor = Color.White,
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.baseline_person_pin_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp))
                },
                placeholder = {
                    Text(text = "Email")
                }
            )

        //    Spacer(modifier = Modifier.height(10.dp))
            
            TextField(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(), value = userPass,
                onValueChange = {userPass = it},
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
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.baseline_key_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp))
                },
                placeholder = {
                    Text(text = "Password")
                }
            )
            Spacer(modifier = Modifier.padding(12.dp))
            ActionButton(text = "Login",
                onCliciked = {
                    if (userEmail.trim()!= "" && userPass.trim()!= ""){
                        authViewModel.login(userEmail.trim(),userPass.trim(), context,navController)
                    }else{
                        Toast.makeText(context, "Enter email/password", Toast.LENGTH_LONG).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPinkDark,
                    contentColor = Color.White
                ), shadowColor = PrimaryPinkDark,
                modifier = Modifier.padding(horizontal = 24.dp))


            Text(text = "Dont have an Account?",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black, modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center
            )
            ActionButton(text = "Sign Up",
                onCliciked = {
                    navController.navigate(Screen.createAcc.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPinkLight,
                    contentColor = Color.White
                ), shadowColor = PrimaryPinkBlended,
                modifier = Modifier.padding(horizontal = 24.dp))
        }
    }
}

@Composable
fun message(
    modifier: Modifier = Modifier,
    title:String,
    subTitle: String){

    Text(
        text = title,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
        color = Color.White,
        fontWeight = FontWeight.Medium
    )

    Text(
        text = subTitle,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineMedium,
        color = Color.White,
        fontWeight = FontWeight.Medium
    )

}

@Composable
fun ActionButton(
    modifier: Modifier,
    text: String,
    onCliciked: ()-> Unit,
    colors: ButtonColors,
    shadowColor: Color
) {
    Button(onClick = onCliciked,
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .padding(horizontal = 24.dp)
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(50),
                spotColor = shadowColor
            ),
        colors = colors) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold)
        }
    }
}
