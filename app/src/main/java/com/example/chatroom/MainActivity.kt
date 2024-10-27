package com.example.chatroom

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatroom.ui.theme.ChatRoomTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.initialize

//import com.example.chatroom.ui.theme.PhoneAuthActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context: Context = this
            val authViewModel: AuthViewModel = viewModel()
            val messageViewModel: messageViewModel = viewModel()
            val profPhotoViewModel: profPhotoViewModel = viewModel()
            ChatRoomTheme {
                Firebase.initialize(this)
                Navigation(authViewModel = authViewModel, context = context, user = User("","","",""),
                    messageViewModel = messageViewModel, photoViewModel = profPhotoViewModel)
            }
        }
    }
}

