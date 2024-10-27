package com.example.chatroom

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalFocusManager
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
fun addFriend(authViewModel: AuthViewModel, context: Context, email:String, navController: NavController,
                  profPhotoViewModel: profPhotoViewModel, friendViewModel: friendViewModel) {
    //authViewModel.getUser(context)
    val Users: State<List<User>> = authViewModel.totalUsers.collectAsState(emptyList())
    val loggedInUser = Users.value.find {emailId->
        emailId.email == email }

    val contactList = Users.value - loggedInUser
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route
    val parsedRoute = currentRoute?.substringBefore("/")
    var searchedName by remember {
        mutableStateOf("")
    }
    var showContact by remember {
        mutableStateOf(false)
    }
    var showFriendRequest by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    Scaffold(
        bottomBar = {
            if (parsedRoute != null) {
                bottomAppBar(navController = navController, route = parsedRoute, email = email )
            }
        },
        topBar = {
                MyAppBar(authViewModel,context,
                    navController
                )
        }
    ) {
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .background(color = backgroundColor)
        ){
            Text(text = "Add Friend",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp,
                    vertical = 12.dp),
                letterSpacing = 1.sp)
            Spacer(modifier = Modifier.padding(4.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {

                TextField(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(50)),
                    value = searchedName,
                    onValueChange = { searchedName = it
                        showContact= false},
                    singleLine = true,
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
                    trailingIcon = {
                        Icon(painter = painterResource(R.drawable.baseline_search_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    if (searchedName.trim() != "") {
                                        showContact = true
                                        showFriendRequest = false
                                        focusManager.clearFocus()
                                    } else {
                                        Toast
                                            .makeText(
                                                context,
                                                "Enter Name to search",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                }
                        )
                    },
                    placeholder = {
                        Text(text = "Search")
                    }
                )
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(60.dp)
                        .weight(1f)
                        .clickable {

                        }
                ) {
                    friendRequestBadge(friendViewModel = friendViewModel, context = context, userEmail = email)
                    {requestCount->
                        if(requestCount>0){
                            if (showFriendRequest) {
                                showFriendRequest = false
                            } else {
                                showFriendRequest = true
                            }
                            showContact = false
                        }

                    }
                }

            }

            Spacer(modifier = Modifier.padding(8.dp))


            if(showContact){
                contactTile(
                    friendViewModel = friendViewModel,
                    userEmail = email,
                    profPhotoViewModel = profPhotoViewModel,
                    name = searchedName.trim(),
                    contactList = contactList,
                    context = context,
                )

            }
            if(showFriendRequest){
                showRequests(
                    friendViewModel = friendViewModel,
                    context = context,
                    currentUserEmail = email,
                    authViewModel = authViewModel,
                    profPhotoViewModel = profPhotoViewModel,
                )

            }

        }
    }
}

@Composable
fun contactTile(friendViewModel: friendViewModel, userEmail: String,
                profPhotoViewModel: profPhotoViewModel, name: String,
                contactList: List<User?>, context: Context,

                ) {
    val user = contactList.filter { it?.firstName?.lowercase() == name.lowercase() }
    var searchedUser: List<User?> = contactList
    if (user.isEmpty()){
        Text(text = "No user found",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
            fontSize = 22.sp,
        )
    }
    LazyColumn(modifier = Modifier
        .padding(12.dp)) {
        items(searchedUser) { user ->
            friendViewModel.getFriendList(context)
            var friendList = friendViewModel.friendList.value

            if (user?.firstName?.lowercase() == name.lowercase() &&
                friendList.find { it.senderEmail == user.email && it.receiverEmail== userEmail && it.requestAccepted } == null
                && friendList.find { it.senderEmail == userEmail && it.receiverEmail== user.email && it.requestAccepted }== null
                && friendList.find { it.senderEmail == user.email && it.receiverEmail== userEmail && !it.requestAccepted }== null) {
                val selectedUser = user.email
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),

                    ) {

                    Row(modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Row {


                            user.email.let { it1 ->
                                if (it1 != null) {
                                    profPhotoViewModel.getImage(it1)
                                }
                            }
                            val contactprofPhoto =
                                profPhotoViewModel.savedImage.value.find { it.email == user.email }

                            Surface(modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp), shape = CircleShape) {

                                if (contactprofPhoto?.email == user.email) {
                                    contactprofPhoto.photo?.let { it1 ->
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
                                if (user != null) {
                                    Text(
                                        text = user.firstName + " " + user.lastName,
                                        fontSize = 22.sp,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Medium

                                    )
                                }
                            }
                        }

                        if( friendList.find { it.senderEmail == userEmail && it.receiverEmail== user.email && !it.requestAccepted }== null
                            ){
                                Image(painter = painterResource(id = R.drawable.baseline_person_add_alt_24),
                                    contentDescription = "send friend request",
                                    modifier = Modifier
                                        .size(42.dp)
                                        .align(Alignment.CenterVertically)
                                        .padding(8.dp)
                                        .clickable {
                                            if (selectedUser != null) {
                                                friendViewModel.sendRequest(
                                                    userEmail,
                                                    selectedUser,
                                                    context
                                                )
                                                friendViewModel.getFriendList(context)
                                                searchedUser = searchedUser - user
                                            }

                                        }
                                )
                            }
                            else if(
                                friendList.find { it.senderEmail == userEmail && it.receiverEmail== user.email && !it.requestAccepted }!= null
                            )
                            {
                                Image(imageVector = Icons.Default.Check, contentDescription = "Request Sent",
                                    modifier = Modifier
                                        .size(42.dp)
                                        .align(Alignment.CenterVertically)
                                        .padding(8.dp)
                                )
                            }

                    }
                }
            }
        }
    }
}

@Composable
fun showRequests(friendViewModel: friendViewModel,
                 context: Context,
                 currentUserEmail: String,
                 authViewModel: AuthViewModel,
                 profPhotoViewModel: profPhotoViewModel,
) {
    friendViewModel.getRequest(context)
    val friendRequests = friendViewModel.friendRequests.value.filter { !it.requestAccepted }
    val user = authViewModel.totalUsers.collectAsState(emptyList())

    LazyColumn(modifier = Modifier.padding(12.dp)) {
        items(friendRequests) { friendRequest ->
            val sendersProf =
                user.value.find { it.email == friendRequest.senderEmail }
            if (friendRequest.receiverEmail == currentUserEmail && !friendRequest.requestAccepted) {


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            sendersProf?.email.let { it1 ->
                                if (it1 != null) {
                                    profPhotoViewModel.getImage(it1)
                                }
                            }
                            val contactprofPhoto =
                                profPhotoViewModel.savedImage.value.find { it.email == sendersProf?.email }

                            Surface(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(4.dp), shape = CircleShape
                            ) {

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

                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (sendersProf != null) {
                                    Text(
                                        text = sendersProf.firstName + " " + sendersProf.lastName,
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Medium

                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .heightIn(24.dp)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Icon(imageVector = Icons.Default.Check, contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        friendViewModel.updateRequest(
                                            friendRequest.docId, friendRequest.senderEmail,
                                            friendRequest.receiverEmail, context
                                        )
                                    }
                                    .size(24.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Icon(imageVector = Icons.Default.Close, contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        friendViewModel.deleteRequest(
                                            requestId = friendRequest.docId
                                        )

                                    }
                                    .size(24.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun friendRequestBadge(friendViewModel: friendViewModel, context: Context, userEmail: String,
                       showFriendRequest: (Int) -> Unit) {
    friendViewModel.getRequest(context)
    val friendRequests = friendViewModel.friendRequests.value.filter { !it.requestAccepted && it.receiverEmail == userEmail}

    var itemCount by remember { mutableStateOf(0) }
    itemCount = friendRequests.size

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BadgedBox(
            badge = {
                if (itemCount > 0) {
                    Badge(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ) {
                        Text("$itemCount")
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Shopping cart",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { showFriendRequest(itemCount) }
            )
        }

    }
}