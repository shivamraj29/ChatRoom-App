package com.example.chatroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class messageViewModel: ViewModel() {
    val database = Firebase.database
    val allMessages = mutableStateOf<List<MessageDtl>>(emptyList())
    private val _msgs = MutableStateFlow<List<MessageDtl>>(emptyList())
    val msgs: Flow<List<MessageDtl>> = _msgs
    val isLoading =
        MutableStateFlow(true)


    val myRef = database.getReference("userMessages")

    fun addMessage(email:String,userEmail: String,message:String, timestamp: Long, randomKey: String){
        val inp = MessageInfo(email, userEmail, message, timestamp, randomKey)
        val messageId = myRef.push().key
        if (messageId != null) {
            myRef.child(messageId).setValue(inp)

            myRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val msgs =snapshot.children.mapNotNull { it.getValue(MessageDtl::class.java) }
                    //allMessages.value = msgs
                    val received_msgs = msgs.filter { it.sender == email || it.receiver==email }
                    _msgs.value = received_msgs
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
            )

        }
    }
    fun getMessages(email:String) {


            myRef.get().addOnSuccessListener { data ->
                val fetchedItem = data.children.mapNotNull { it.getValue(MessageDtl::class.java) }
                //allMessages.value = fetchedItem
                val received_msgs = fetchedItem.filter { it.sender == email || it.receiver==email }
                _msgs.value = received_msgs
                isLoading.value = false

            }

        }

}