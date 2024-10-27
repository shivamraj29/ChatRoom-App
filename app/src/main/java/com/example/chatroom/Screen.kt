package com.example.chatroom

sealed class Screen(val route:String) {
    object loginScreen: Screen(route = "loginscreen")
    object createAcc:Screen("createacc")
    object loginSuccess: Screen("loginsuccess")
    object contactScreen:Screen("contactscreen")
    object chatScreen:Screen("chatscreen")
    object accoutInfo:Screen(route = "accountInfo")
    object checkVerifyEmail:Screen("verifyemail")
    object addFriend: Screen("addfriend")
}