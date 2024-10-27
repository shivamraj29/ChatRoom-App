package com.example.chatroom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

class profPhotoViewModel: ViewModel(){

    val authViewModel: AuthViewModel =AuthViewModel()

    val storage = Firebase.storage

    val storageRef = storage.reference.child("profile photo")
    var savedImage = mutableStateOf<List<profPhoto>>(emptyList())


    fun saveImage(bitmap: Bitmap, context: Context, email: String){
    val storeRef = storageRef.child(email)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val data = baos.toByteArray()
    var uploadTask = storeRef.putBytes(data)
     uploadTask .addOnCompleteListener{task->
            Toast.makeText(context, "image uploaded", Toast.LENGTH_LONG).show()
        }.addOnFailureListener{e->
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

}

    fun getImage(email: String) {
        val maxBytes: Long = 1024*1024
        val storeRef = storageRef.child(email)
        storeRef.getBytes(maxBytes).addOnCompleteListener{byteArray->
            if (byteArray.isSuccessful){
            val bitmap = BitmapFactory.decodeByteArray(byteArray.result, 0, byteArray.result.size)
            val newProfPhoto = profPhoto(email = email, photo = bitmap)
            savedImage.value = savedImage.value + newProfPhoto

        }
        }
    }

    fun getProfilePhoto(email: String, password:String, context: Context){
        val maxBytes: Long = 1024*1024
        val storeRef = storageRef.child(email)
        storeRef.getBytes(maxBytes).addOnCompleteListener{byteArray->
            if (byteArray.isSuccessful){
                val bitmap = BitmapFactory.decodeByteArray(byteArray.result, 0, byteArray.result.size)

                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                if (bitmap != null) {
                    authViewModel.updateUser(
                        dataToCache(
                            email = email, password = password,
                            profPic = data
                        )
                    )
                }
            }
        }
    }
}