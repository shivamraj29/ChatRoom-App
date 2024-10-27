package com.example.chatroom

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow


class friendViewModel: ViewModel() {

    private val _friendRequests = mutableStateOf<List<friendRequests>>(emptyList())
    val friendRequests: State<List<friendRequests>> = _friendRequests
    private val _friendList = mutableStateOf<List<friendRequests>>(emptyList())
    val friendList:State<List<friendRequests>> = _friendList
    val isLoading =
        MutableStateFlow(true)

    fun sendRequest(senderEmail: String, receiverEmail: String, context: Context) {

        val db = Firebase.firestore

        val requests = friendRequests(
            docId = "",
            senderEmail= senderEmail,
            receiverEmail = receiverEmail,
            requestSent = true,
            requestAccepted = false
        )
        db.collection("friendRequests").add(requests)
            .addOnSuccessListener { documentRef->
                val documentId = documentRef.id
                val updateData = requests.copy(docId = documentId)
                documentRef.set(updateData)
                    .addOnCompleteListener {

                    }
                Toast.makeText(context, "Friend Request Sent", Toast.LENGTH_LONG)
            }
            .addOnFailureListener{e->
                Log.w(TAG, "Error adding doc" , e)

            }

    }

    fun getRequest(context: Context){
        val db = Firebase.firestore
        db.collection("friendRequests").get()
            .addOnSuccessListener {documentSnapshot->
                if(!documentSnapshot.isEmpty){
                    val fetchedRequest = documentSnapshot.map { doc->
                        friendRequests(
                            docId = doc.getString("docId")?: "",
                            receiverEmail = doc.getString("receiverEmail")?:"",
                            requestAccepted = doc.getBoolean("requestAccepted")?: false,
                            requestSent = doc.getBoolean("requestSent")?:true,
                            senderEmail = doc.getString("senderEmail")?: "",)
                    }
                    _friendRequests.value = fetchedRequest
                    isLoading.value= false
                }

            }
    }

    fun getFriendList(context: Context){
        val db = Firebase.firestore
        db.collection("friendRequests").get()
            .addOnSuccessListener {documentSnapshot->
                if(!documentSnapshot.isEmpty){
                    val fetchedRequest = documentSnapshot.map { doc->
                        friendRequests(
                            docId = doc.getString("docId")?: "",
                            receiverEmail = doc.getString("receiverEmail")?:"",
                            requestAccepted = doc.getBoolean("requestAccepted")?: true,
                            requestSent = doc.getBoolean("requestSent")?:true,
                            senderEmail = doc.getString("senderEmail")?: "",)
                    }
                    _friendList.value = fetchedRequest
                    isLoading.value= false
                }

            }
    }

    fun updateRequest(requestId: String,senderEmail: String, receiverEmail: String , context: Context){
        val db = Firebase.firestore
        val reqRef = db.collection("friendRequests").document(requestId)
        val updatedData = friendRequests(
            docId = requestId,
            senderEmail = senderEmail,
            receiverEmail = receiverEmail,
            requestSent = true,
            requestAccepted = true
        )
        reqRef.set(updatedData)
            .addOnSuccessListener {
                _friendRequests.value = _friendRequests.value.filter { it.docId != requestId }
                _friendList.value = _friendList.value + updatedData
                Toast.makeText(context, "Friend Request Accepted", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener(
            ){
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
    }

    fun deleteRequest(requestId: String){
        val db = Firebase.firestore
        val reqRef = db.collection("friendRequests").document(requestId)
        reqRef.delete().addOnSuccessListener {
            _friendRequests.value = _friendRequests.value.filter { it.docId != requestId }
        }
    }


}

