package com.example.chatroom

import android.content.Context
import androidx.compose.runtime.Composable
//import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


@Composable
fun Navigation(navHostController: NavHostController = rememberNavController(), authViewModel: AuthViewModel
, context: Context, user: User , messageViewModel: messageViewModel, photoViewModel: profPhotoViewModel){
    NavHost(navController= navHostController, startDestination = Screen.loginScreen.route){

        composable(Screen.loginScreen.route) {
           logInScreen(authViewModel = authViewModel, navController = navHostController, context = context)
        }
        composable(Screen.createAcc.route) {
            createAccount(authViewModel,navHostController,context)
        }
        composable(Screen.checkVerifyEmail.route){ 
            checkVerifyEmail(navController = navHostController)
        }
        composable(Screen.loginSuccess.route)
        { loginSuccess(authViewModel, context, navHostController, messageViewModel, photoViewModel,)
        }

        composable(Screen.contactScreen.route + "/{email}",
            arguments = listOf(
                navArgument("email"){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable= false
                }
            )
        ){entry->
            val email= if (entry.arguments!= null) entry.arguments!!.getString("email") else ""
            if (email != null) {
                contactScreen(authViewModel = authViewModel, context = context, email, navHostController, photoViewModel,
                    friendViewModel())
            }
        }
        composable(Screen.chatScreen.route+ "/{selectedUser}" + "/{email}",
            arguments = listOf(
                navArgument("selectedUser"){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = false
                }
            , navArgument("email"){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable= false
                })
        ) {entry->
            val selectedUser = if(entry.arguments!= null) entry.arguments!!.getString("selectedUser") else ""
            val userEmail = if(entry.arguments!= null) entry.arguments!!.getString("email") else ""
            if (selectedUser != null&& userEmail!= null) {
                chatScreen(selectedUser,userEmail ,messageViewModel, authViewModel, photoViewModel, navHostController)
            }
        }
        composable(Screen.accoutInfo.route+ "/{email}",
            arguments = listOf(
                navArgument("email"){
                    type= NavType.StringType
                    defaultValue = ""
                    nullable= false
                }
            )
        ) {entry->
            val userEmail = if(entry.arguments!= null) entry.arguments!!.getString("email") else ""
            if (userEmail != null) {
                accountInfo(email = userEmail, authViewModel =authViewModel , navController = navHostController, profPhotoViewModel(),context)
            }

        }

        composable(Screen.addFriend.route + "/{email}",
            arguments = listOf(
                navArgument("email"){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable= false
                }
            )
        ) { entry->
            val email = if(entry.arguments!= null) entry.arguments!!.getString("email") else ""
            if (email != null) {
                addFriend(
                    authViewModel = authViewModel,
                    context = context,
                    email = email,
                    navController = navHostController,
                    profPhotoViewModel = photoViewModel,
                    friendViewModel = friendViewModel()
                )
            }
        }
    }
}