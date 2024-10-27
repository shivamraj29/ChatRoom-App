package com.example.chatroom

import android.content.Context

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.chatroom.ui.theme.backgroundColor
import com.example.chatroom.ui.theme.darkTextColor

@Composable
fun contactScreen(authViewModel: AuthViewModel, context: Context, email:String, navController: NavController,
                  profPhotoViewModel: profPhotoViewModel, friendViewModel: friendViewModel) {

    val Users: State<List<User>> = authViewModel.totalUsers.collectAsState(emptyList())

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route
    val parsedRoute = currentRoute?.substringBefore("/")


    Scaffold(
        bottomBar = {
            if (parsedRoute != null) {
                bottomAppBar(navController = navController, route = parsedRoute, email = email )
            }
        },
        topBar = {
                MyAppBar( authViewModel,context,
                    navController,
                )

        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.addFriend.route+ "/$email")  },
                shape = FloatingActionButtonDefaults.extendedFabShape,
                containerColor = Color(17,94,84),
                contentColor = Color.White
            ){
                Icon(painter = painterResource(R.drawable.baseline_person_add_alt_24), contentDescription = null)
            }
        }
    ) {
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .background(color = backgroundColor)
            ){
            Text(text = "Friends",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp,
                    vertical = 12.dp),
                letterSpacing = 1.sp)

            Spacer(modifier = Modifier.padding(8.dp))

           friendList(friendViewModel = friendViewModel, context = context,
                currentUserEmail = email,
                profPhotoViewModel = profPhotoViewModel,
               navController = navController,
               user = Users
               )
    }
}
}


@Composable
fun friendList(friendViewModel: friendViewModel, context: Context, currentUserEmail: String
                ,profPhotoViewModel: profPhotoViewModel,
               navController: NavController, user: State<List<User>>){
    friendViewModel.getFriendList(context)
    val isLoading = friendViewModel.isLoading.collectAsState()
    val friendList = friendViewModel.friendList.value.filter { it.requestAccepted }
    val totalFriends = friendList.filter {it.senderEmail == currentUserEmail || it.receiverEmail== currentUserEmail}

    LazyColumn (modifier = Modifier.padding(12.dp)){
        items(friendList){friendList->
            if(totalFriends.isNullOrEmpty() && !isLoading.value){
                Text(text = "Add Friends to start chatting",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 20.sp,
                )
            }
            if (friendList.senderEmail== currentUserEmail|| friendList.receiverEmail == currentUserEmail){
           var sendersProf: User?=null

            if(friendList.receiverEmail== currentUserEmail) {
                 sendersProf = user.value.find { it.email == friendList.senderEmail }
            }else if(
                friendList.senderEmail== currentUserEmail
            ){
                sendersProf = user.value.find { it.email == friendList.receiverEmail }
            }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { navController.navigate(Screen.chatScreen.route + "/${sendersProf?.email}" + "/$currentUserEmail") },
                    colors = CardDefaults.cardColors(containerColor = Color.White,
                        contentColor = darkTextColor)
                ) {

                    Row(modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Row {
                            sendersProf?.email.let { it1 ->
                                if (it1 != null) {
                                    profPhotoViewModel.getImage(it1)
                                }
                            }
                            val contactprofPhoto =
                                profPhotoViewModel.savedImage.value.find { it.email == sendersProf?.email }

                            Surface(modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp), shape = CircleShape) {

                                if (contactprofPhoto?.email == sendersProf?.email) {
                                    contactprofPhoto?.photo?.let { it1 ->
                                        Image(
                                            bitmap = it1.asImageBitmap(),
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.userprofile),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )

                                }
                            }

                            Column(modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.Center) {
                                if (sendersProf != null) {
                                    Text(
                                        text = sendersProf.firstName + " " + sendersProf.lastName,
                                        fontSize = 16.sp,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold

                                    )
                                }
                            }
                        }

                    }

                }

        }
        }
    }
    
}

