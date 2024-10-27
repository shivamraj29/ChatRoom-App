package com.example.chatroom


import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.util.Log
import android.widget.Toast

import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class AuthViewModel(
    val dataRespository: dataRespository = Graph.dataRepository
): ViewModel() {



    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    var loginFail = mutableStateOf(false)
    private val itemss = MutableStateFlow<List<User>>(emptyList())
    val totalUsers: Flow<List<User>> = itemss


    init {
        getUser()
    }
    val isLoading = MutableStateFlow(true)

    fun addUser(dataToCache: dataToCache){
        viewModelScope.launch(Dispatchers.IO) {
        dataRespository.addUser(dataToCache)
        }
    }

    lateinit var getUser: Flow<dataToCache>

    init {
        viewModelScope.launch {
           // delay(5000)
            getUser = dataRespository.getUser()
            isLoading.value = false
        }
    }

    fun deleteUser(dataToCache: dataToCache){
        viewModelScope.launch(Dispatchers.IO) {
            dataRespository.deleteData(dataToCache)
        }
    }
    fun updateUser(dataToCache: dataToCache){
        viewModelScope.launch(Dispatchers.IO) {
            dataRespository.updateData(dataToCache)
        }
    }



    fun login(email: String, password: String, context: Context, navController: NavController){
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    val user = auth.currentUser
                    if (task.isSuccessful&& user?.isEmailVerified == true){
                        addUser(dataToCache(email = email, password = password, profPic = drawableToByteArray(context)))
                        //Toast.makeText(context,"Login Successfull ", Toast.LENGTH_LONG ).show()
                           navController.navigate(Screen.loginSuccess.route ){
                            popUpTo(Screen.loginScreen.route){inclusive = true} }



                    }else{
                        Toast.makeText(context,"Incorrect Email/Password", Toast.LENGTH_LONG ).show()

                    }
                }
    }

    fun signup(firstName: String, lastName: String, email: String,
               password: String, navController: NavController, context: Context){

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){

                    addUser(firstName,lastName,email,password)
                    Toast.makeText(context, "Account Created!", Toast.LENGTH_LONG).show()
                    verifyEmail(context)
                    navController.navigate(Screen.checkVerifyEmail.route)

                }else{
                    loginFail.value = true


                    Toast.makeText(context, task.exception?.message , Toast.LENGTH_LONG).show()
                }
            }
    }

    fun addUser(firstName: String, lastName: String, email: String, password: String) {

        val db = Firebase.firestore

        val user = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "password" to password
        )
        db.collection("users").add(user)
            .addOnCompleteListener{documentRef->

                Log.d(TAG, "Document added with id $documentRef")
            }
            .addOnFailureListener{e->
                Log.w(TAG, "Error adding doc" , e)

            }

    }

    fun getUser(){
        val db = Firebase.firestore
        db.collection("users").get()
            .addOnSuccessListener { documentSnapshot ->
                if(!documentSnapshot.isEmpty){
                    val fetchedItems = documentSnapshot.map { doc ->
                        User(firstName = doc.getString("firstName")?:"", lastName = doc.getString("lastName") ?: "",
                            email = doc.getString("email")?:"", password = doc.getString("password")?: "")
                }

                    itemss.value = fetchedItems

                }
            }
            .addOnFailureListener{

        }
    }

    fun verifyEmail(context: Context){
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {
            Toast.makeText(context, "link sent", Toast.LENGTH_LONG).show()
        }?.addOnFailureListener{
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

        }
    }

}




fun drawableToByteArray(context: Context): ByteArray {
    // Load the drawable resource as a Bitmap
    val resourceId = R.drawable.userprofile
    val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)

    // Convert the Bitmap to a ByteArray
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) // Use PNG or JPEG
    return stream.toByteArray()
}
