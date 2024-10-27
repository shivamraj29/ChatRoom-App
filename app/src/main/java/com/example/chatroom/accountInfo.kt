package com.example.chatroom

import android.content.Context

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth

import java.io.ByteArrayOutputStream

@Composable
fun accountInfo(email: String, authViewModel: AuthViewModel, navController: NavController, profPhotoViewModel: profPhotoViewModel,
                context: Context){


    val users: State<List<User>> = authViewModel.totalUsers.collectAsState(emptyList())
    val currentUser = users.value.find { it.email == email }
    val user = authViewModel.getUser.collectAsState(initial = dataToCache(email = "", password = "", profPic = drawableToByteArray(context)))

    //Navigation
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentPage = backStackEntry?.destination?.route
    val parsedRoute = currentPage?.substringBefore("/")

    //Getting Image from gallery
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var deleteClicked by remember {
        mutableStateOf(false)
    }

    var cachedbitmap = BitmapFactory.decodeByteArray(drawableToByteArray(context), 0, drawableToByteArray(context).size)
    if(!deleteClicked) {
        cachedbitmap = BitmapFactory.decodeByteArray(user.value.profPic, 0, user.value.profPic.size)
    }

    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uri:Uri? ->
        imageUri=uri
    }

    imageUri?.let {
        if (Build.VERSION.SDK_INT < 28){
            bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        }else{
            val source = ImageDecoder.createSource(context.contentResolver, it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }
    }
    var selectPhoto = remember {
    mutableStateOf(false)}
    var updatePhoto = remember {
        mutableStateOf(false)}



    Scaffold(
        topBar = {
            if(!deleteClicked){
            MyAppBar(
            authViewModel,context,
                navController)}},
        bottomBar = {
            if (parsedRoute != null) {
                bottomAppBar(navController = navController,
                    route =parsedRoute , email =email )
            }
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize(),

            ) {
            Text(text = "Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp,
                    vertical = 8.dp),
                letterSpacing = 1.sp)

                Card(shape = CardDefaults.elevatedShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)

                ) {
                Row (modifier = Modifier
                    .padding()
                    .fillMaxWidth()
                    .background(
                        color = Color(240, 240, 240)
                    ), horizontalArrangement = Arrangement.SpaceBetween){
                    Column (modifier = Modifier.padding(22.dp)){
                    Row() {
                        Surface(
                            modifier = Modifier.size(120.dp), shape = CircleShape,
                            shadowElevation = 16.dp, tonalElevation = 16.dp
                        ) {
                            if (cachedbitmap!= null) {

                                if (bitmap.value == null) {

                                    Image(
                                        bitmap = cachedbitmap.asImageBitmap(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                } else if (bitmap.value != null && selectPhoto.value) {
                                    Image(
                                        bitmap = bitmap.value!!.asImageBitmap(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        bitmap = cachedbitmap.asImageBitmap(),
                                        contentDescription = null, contentScale = ContentScale.Crop
                                    )

                                }
                            }
                        }

                        Image(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = (-30).dp, y = (0).dp)
                                .background(Color(1, 2, 3, 90), shape = CircleShape)
                                .size(40.dp)
                                .padding(8.dp)
                                .clickable {
                                    launcher.launch("image/*")
                                    selectPhoto.value = true
                                    updatePhoto.value = true
                                }
                                .align(Alignment.Bottom)
                                    ,

                            colorFilter = ColorFilter.tint(Color.White),

                            )


                    }
                    if (updatePhoto.value) {
                        Box(modifier = Modifier
                            .clickable {
                                bitmap.value.let { bmp ->
                                    val bit = BitmapDrawable(context.resources, bmp).bitmap
                                    if (bit != null) profPhotoViewModel.saveImage(
                                        bit,
                                        context,
                                        email
                                    )
                                    profPhotoViewModel.getImage(email)
                                    val baos = ByteArrayOutputStream()
                                    bit.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                    val data = baos.toByteArray()
                                    authViewModel.updateUser(
                                        dataToCache(
                                            email = user.value.email,
                                            password = user.value.password, profPic = data
                                        )
                                    )
                                    selectPhoto.value = false
                                    updatePhoto.value = false
                                }
                            }
                            .padding(16.dp)
                            .background(
                                Color(128, 220, 190, 80),
                                shape = RoundedCornerShape(16.dp)
                            )
                        ) {
                            Text(text = "Update DP", modifier = Modifier.padding(8.dp))
                        }
                    }
                    }
                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center) {

                        currentUser?.firstName?.let { it1 ->
                            if (currentUser.lastName != ""){
                            Text(
                                text = it1.uppercase() + " " + currentUser.lastName.uppercase(), fontSize = 24.sp,
                                textAlign = TextAlign.End, modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(vertical = 12.dp),
                                overflow = TextOverflow.Clip

                            )
                            }else{
                                Text(
                                    text = it1.uppercase(), fontSize = 24.sp,
                                    textAlign = TextAlign.End, modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(vertical = 12.dp)

                                )

                            }
                        }
                        if (currentUser != null) {
                            Text(
                                text = currentUser.email.substringBefore("@"),
                                fontSize = 18.sp,

                                textAlign = TextAlign.End
                            )
                            Text(
                                text = "@" + currentUser.email.substringAfter("@"),
                                fontSize = 10.sp,

                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }

            Button(onClick = { val auth = FirebaseAuth.getInstance()
                                auth.signOut()
                               // authViewModel._authState.value = AuthState.unauthenticated
                                navController.navigate(Screen.loginScreen.route)
                                deleteClicked = true
                                authViewModel.deleteUser(
                                    user.value
                                )
            },
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = "Sign Out")
            }
        }
    }
}


