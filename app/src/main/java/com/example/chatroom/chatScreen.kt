package com.example.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatroom.ui.theme.darkTextColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Composable
fun chatScreen(email: String, userEmail:String ,messageViewModel: messageViewModel, authViewModel: AuthViewModel,
               profPhotoViewModel: profPhotoViewModel, navController: NavController) {
    var message by remember {
        mutableStateOf("")
    }

    val User = authViewModel.totalUsers.collectAsState(emptyList())
    val selectedUser = User.value.find { it.email == email }
    val keyEmail = email.replace(".", ":")
    val keySenderEmail = userEmail.replace(".", ":")
    messageViewModel.getMessages(keyEmail)
    val msgToBeReversed = messageViewModel.msgs.collectAsState(emptyList()).value
   // val msgToBeReversed = messageViewModel.allMessages.value
    val msgToShow = msgToBeReversed.reversed()
    val current = System.currentTimeMillis()
    val groupedByConvo = getFirstMessagesByDateAndConversation(msgToBeReversed)
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            if (selectedUser != null) {
                chatAppBar(selectedUser, profPhotoViewModel, navController)
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .weight(1f)
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth(),
                    reverseLayout = true) {
                    items(msgToShow) { msg ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Column {

                                if (msg.sender == keySenderEmail && msg.receiver == keyEmail
                                    || msg.sender == keyEmail && msg.receiver == keySenderEmail) {
                                    for (f in groupedByConvo) {
                                        if (f.message == msg.message && f.sender == msg.sender && f.receiver == msg.receiver
                                            && f.randomKey == msg.randomKey && msg.message != ""
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.CenterHorizontally)
                                                    .padding(10.dp)
                                                    .background(
                                                        color = Color(240, 240, 240, 95),
                                                        shape = RoundedCornerShape(16.dp)
                                                    )
                                                    .shadow(
                                                        8.dp,
                                                        shape = CircleShape,
                                                        spotColor = Color.White
                                                    )
                                            )
                                            {
                                                Text(
                                                    text = checkSameDate(f.timestamp, current),
                                                    fontSize = 16.sp,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    modifier = Modifier.padding(
                                                        vertical = 4.dp,
                                                        horizontal = 12.dp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    if (msg.sender == keySenderEmail && msg.receiver == keyEmail) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Card(
                                                shape = RoundedCornerShape(16.dp),
                                                elevation = CardDefaults.cardElevation(),
                                                modifier = Modifier
                                                    .wrapContentSize()
                                                    .widthIn(max = 300.dp)
                                                    .padding(8.dp),
                                                colors = CardDefaults.elevatedCardColors(
                                                    Color(
                                                        0,
                                                        122,
                                                        255,
                                                    )
                                                )
                                            ) {
                                                Box(modifier = Modifier.align(Alignment.End)) {
                                                    Column() {
                                                        Text(
                                                            text = msg.message, modifier = Modifier
                                                                .padding(
                                                                    top = 12.dp,
                                                                    start = 16.dp,
                                                                    end = 16.dp
                                                                ),
                                                            fontSize = 16.sp,
                                                            color = Color.White,
                                                            style = MaterialTheme.typography.labelMedium,
                                                            lineHeight = 24.sp
                                                        )
                                                        Row(
                                                            horizontalArrangement = Arrangement.End,
                                                            modifier =
                                                            Modifier.align(Alignment.End)
                                                        ) {
                                                            Text(
                                                                text = formatTime(msg.timestamp),
                                                                textAlign = TextAlign.End,
                                                                fontSize = 12.sp,
                                                                modifier = Modifier.padding(end = 8.dp),
                                                                color = Color.LightGray
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else if (msg.sender == keyEmail && msg.receiver == keySenderEmail) {
                                        Spacer(modifier = Modifier.padding(12.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Card(
                                                shape = RoundedCornerShape(16.dp),
                                                elevation = CardDefaults.cardElevation(),
                                                modifier = Modifier
                                                    .wrapContentSize()
                                                    .widthIn(max = 300.dp)
                                                    .padding(8.dp),
                                                colors = CardDefaults.elevatedCardColors(
                                                    Color.LightGray
                                                )
                                            ) {
                                                Box {
                                                    Column(modifier = Modifier) {
                                                        Text(
                                                            text = msg.message, modifier = Modifier
                                                                .padding(
                                                                    top = 12.dp,
                                                                    start = 16.dp,
                                                                    end = 16.dp,
                                                                ),
                                                            fontSize = 16.sp,
                                                            style = MaterialTheme.typography.labelMedium,
                                                            lineHeight = 24.sp
                                                        )
                                                        Row(
                                                            modifier = Modifier
                                                                .align(Alignment.End)
                                                                .padding()
                                                        ) {
                                                            Text(
                                                                text = formatTime(msg.timestamp),
                                                                fontSize = 12.sp,
                                                                modifier = Modifier.padding(end = 8.dp)
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
                    }
                }
            }
            Column(modifier = Modifier
                .heightIn(100.dp)
                .fillMaxWidth()
                .background(color = Color(209, 203, 190, 100)),
                verticalArrangement = Arrangement.Bottom) {

                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    TextField(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(50))
                            .weight(1f),
                        value = message,
                        onValueChange = { message = it },
                        singleLine = false,
                        shape = RoundedCornerShape(50),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
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
                            Text(text = "Message")
                        }
                    )
                    IconButton(onClick = {
                            val messageContent = message.trim()
                        if (messageContent != ""){
                        messageViewModel.addMessage(keyEmail, keySenderEmail, message.trim(),current,
                            generateRandomKey(16))
                        message = ""
                        focusManager.clearFocus()}
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(32.dp) )
                    }
                }

            }
        }
    }
}

fun formatTime(dateTime: Long):String{
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(Date(dateTime))
}

fun formatDate(dateTime: Long):String{
    val dateFormat = SimpleDateFormat("dd LLL", Locale.getDefault())
    return dateFormat.format(dateTime)
}

fun checkSameDate(dateTime:Long, currectTime:Long):String{
    val currentDate = formatDate(currectTime)
    val msgDate = formatDate(dateTime)
    if (msgDate== currentDate){
        return "Today"
    }
    else{
        return msgDate
    }
}

fun generateRandomKey(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { chars[Random.nextInt(chars.length)] }
        .joinToString("")
}


fun getFirstMessagesByDateAndConversation(messages: List<MessageDtl>): List<MessageDtl> {
    val dateMap = mutableMapOf<String, MutableMap<Pair<String, String>, MessageDtl>>()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Iterate over messages and group them by date and conversation
    for (msg in messages) {
        // Format the date
        val dateKey = dateFormat.format(Date(msg.timestamp))

        // Create a unique key for the conversation (order doesn't matter)
        val conversationKey = if (msg.sender < msg.receiver) {
            Pair(msg.sender, msg.receiver)
        } else {
            Pair(msg.receiver, msg.sender)
        }

        // Add the message if it's the first one for that conversation on that date
        val conversationMap = dateMap.getOrPut(dateKey) { mutableMapOf() }
        if (!conversationMap.containsKey(conversationKey)) {
            conversationMap[conversationKey] = msg
        }
    }

    // Flatten the map into a list
    return dateMap.values.flatMap { it.values }.toList()
}
