package com.example.chatroom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(authViewModel: AuthViewModel, context: Context,
             navController: NavController){

    val user = authViewModel.getUser.collectAsState(initial = dataToCache(email = "", password = "", profPic = drawableToByteArray(context)))
    val profPic = BitmapFactory.decodeByteArray(
        user.value.profPic,
        0,
        user.value.profPic.size)

    TopAppBar(title = { Text(text = "")},
        modifier = Modifier.background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFBBDEFB), // Light Blue
                        Color(0xFFC8E6C9)  // Light Green
                    )
                )
                ),
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        navigationIcon = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {

                Icon(imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp))
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp),
                    shape = CircleShape
                ) {
                    Image(
                        bitmap = profPic.asImageBitmap()
                        // painter = painterResource(R.drawable.userprofile)
                        , contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clickable {
                           navController.navigate(Screen.accoutInfo.route + "/${user.value.email}")
                        }
                    )

                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chatAppBar(sender: User, profPhotoViewModel: profPhotoViewModel,
               navController: NavController){

    val senderProf = profPhotoViewModel.savedImage.value.find { it.email == sender.email }
    TopAppBar(title =
    {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(modifier = Modifier.size(50.dp), shape = CircleShape,
                shadowElevation = 8.dp,
                tonalElevation = 8.dp) {
            if (senderProf?.photo!= null){
                Image(bitmap = senderProf.photo.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop)
            }else
            {
                Image(
                    painter = painterResource(id = R.drawable.userprofile),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            }
            Text(text = sender.firstName , fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(8.dp))
        }
    },
        colors = TopAppBarDefaults.topAppBarColors(Color(240,240,240)),
        navigationIcon = { Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null,
            modifier = Modifier
                .clickable {
                    navController.navigateUp()
                }
                .padding(start = 16.dp)
        ) })
}

@Composable
fun bottomAppBar(navController: NavController, route: String, email:String){
    BottomAppBar(modifier = Modifier.fillMaxWidth(),
        containerColor = Color(235,235,235),
        contentColor = Color.Gray,
        tonalElevation = 24.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()
            ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .blur(4.dp)
            )
            val colorFilter = ColorFilter.tint(color = Color.Black)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_chat_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            if (Screen.loginSuccess.route != route)
                                navController.navigate(Screen.loginSuccess.route)
                        },
                    colorFilter =
                    if (Screen.loginSuccess.route == route) colorFilter else ColorFilter.tint(Color.Gray)
                )
                Image(
                    painter = painterResource(id = R.drawable.baseline_people_outline_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            if (Screen.contactScreen.route != route) navController.navigate(Screen.contactScreen.route + "/$email")
                        },
                    colorFilter =
                    if (Screen.contactScreen.route == route) colorFilter else ColorFilter.tint(Color.Gray)
                )
                /* Image(painter = painterResource(id = R.drawable.baseline_person_pin_24), contentDescription =null ,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        if (Screen.accoutInfo.route != route) {
                            navController.navigate(Screen.accoutInfo.route + "/$email")
                        }
                    }, colorFilter =
                if(Screen.accoutInfo.route == route) colorFilter else ColorFilter.tint(Color.Black))*/

            }
        }
    }

}
suspend fun fetchImage(email: String,password:String, context: Context, profPhotoViewModel: profPhotoViewModel){
        profPhotoViewModel.getProfilePhoto(email,password, context)
}


