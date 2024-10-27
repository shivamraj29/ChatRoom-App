package com.example.chatroom

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.chatroom.ui.theme.backgroundColor

@Composable
fun loginSuccess(authViewModel: AuthViewModel, context: Context, navController: NavController, messageViewModel: messageViewModel,
                 profPhotoViewModel: profPhotoViewModel){

    authViewModel.getUser()
    val Users: State<List<User>> = authViewModel.totalUsers.collectAsState(emptyList())

    val currentUser = authViewModel.getUser.collectAsState(initial = dataToCache(email = "", password = "", profPic = drawableToByteArray(context)))
    if (currentUser.value.email!= ""){
        LaunchedEffect(Unit) {
            fetchImage(currentUser.value.email, currentUser.value.password, context, profPhotoViewModel)
        }
    }

    val isLoading = messageViewModel.isLoading.collectAsState(true)
    val email = currentUser.value.email
    val keyEmail = email.replace(".", ":")
    messageViewModel.getMessages(keyEmail)
    val msgs = messageViewModel.msgs.collectAsState(emptyList()).value
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route
    val parsedRoute = currentRoute?.substringBefore("/")


   Scaffold(
       bottomBar = {
           if (parsedRoute != null) {
               bottomAppBar(navController, parsedRoute, email)
           }
       },
       topBar ={
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
           }}

   ) {

    Column(modifier = Modifier
        .padding(it)
        .fillMaxSize()
        .background(color = backgroundColor)) {
        Text(text = "Chats",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp,
                vertical = 12.dp),
            letterSpacing = 1.sp)
        Spacer(modifier = Modifier.padding(4.dp))

        val receivedMsg = msgs.filter { it.receiver== keyEmail || it.sender == keyEmail }


        val lastMsg = getLastMessage(receivedMsg, keyEmail)
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))) {
            items(lastMsg){data->
                if (receivedMsg.isEmpty()&&!isLoading.value){
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Start Chatting",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Go to Add Friends page",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
                else if (isLoading.value){
                    msgTile(
                        firstName = "loading",
                        message = "...",
                        navController = navController,
                        email = "",
                        userEmail = "",
                        profPhotoViewModel =profPhotoViewModel
                    )
                }

                val sendBy = data.sender
                val sendByEmail = sendBy.replace(":",".")
                val senderID = Users.value.find { it.email == sendByEmail }
                val receiver= data.receiver
                val receiverEmail = receiver.replace(":", ".")
                val receiverID = Users.value.find { it.email== receiverEmail }
                if (senderID != null && receiverID !=null) {
                    if (data.sender == keyEmail) {
                        msgTile(firstName = receiverID.firstName, message = data.message, navController, receiverID.email, senderID.email, profPhotoViewModel)
                    }else{
                        msgTile(firstName = senderID.firstName, message = data.message, navController, senderID.email, receiverID.email, profPhotoViewModel)
                    }
                    }

            }
        }

    }
   }

}

@Composable
fun msgTile(firstName: String, message:String, navController: NavController, email: String, userEmail: String,
            profPhotoViewModel: profPhotoViewModel){
    Column() {
        Row (modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .height(100.dp)
            .clickable { navController.navigate(Screen.chatScreen.route + "/$email" + "/$userEmail") }
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically){
            profPhotoViewModel.getImage(email)
            Surface(modifier = Modifier.size(48.dp), shape = CircleShape) {
            val senderProfile = profPhotoViewModel.savedImage.value.find { it.email == email }
                if(senderProfile?.photo != null){
                senderProfile?.photo?.asImageBitmap()?.let {
                    Image(bitmap = it,
                        contentDescription = null, contentScale = ContentScale.Crop)
                }
                }else{
                    Image(painter = painterResource(R.drawable.userprofile), contentDescription = null,
                        contentScale = ContentScale.Crop)
                }

            }
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(text = firstName, fontSize = 18.sp, fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.headlineMedium)
                Text(text = message, overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium)

            }

        }

    }
}

fun getLastMessage(messages: List<MessageDtl>, person: String): List<MessageDtl> {
    val lastMessageMap = mutableMapOf<String, MessageDtl>()

    // First, determine the last message for each conversation
    for (msg in messages) {
        val key = if (msg.sender < msg.receiver) {
            "${msg.sender} - ${msg.receiver}"
        } else {
            "${msg.receiver} - ${msg.sender}"
        }

        // Update the last message for this conversation
        if (msg.receiver == person || msg.sender == person) {
            lastMessageMap[key] = if (lastMessageMap.containsKey(key)) {
                // Keep the most recent message
                if (msg.timestamp > lastMessageMap[key]!!.timestamp) msg else lastMessageMap[key]!!
            } else {
                msg
            }
        }
    }

    // Now filter out messages based on the last sent messages from 'person'
    val resultMessages = mutableListOf<MessageDtl>()
    val lastSentMap = mutableMapOf<String, Long>() // To track the latest sent messages by 'person'

    for (msg in lastMessageMap.values) {
        if (msg.sender == person) {
            lastSentMap[msg.receiver] = msg.timestamp // Track the latest message sent by 'person'
            resultMessages.add(msg) // Always include messages sent by 'person'
        } else if (msg.receiver == person) {
            // Check if the sender's message should be included
            if (lastSentMap[msg.sender]?.let { it > msg.timestamp } != true) {
                resultMessages.add(msg) // Include if there's no more recent sent message
            }
        }
    }

    return resultMessages.sortedByDescending { it.timestamp }
}


