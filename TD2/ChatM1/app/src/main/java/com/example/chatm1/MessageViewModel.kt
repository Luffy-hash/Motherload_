package com.example.chatm1

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.net.URL
import java.util.Date
import javax.xml.parsers.DocumentBuilderFactory

class MessageViewModel : ViewModel() {

    private val repository = MessagesRepo.getInstance()
    val messages: LiveData<List<Message>> = repository.messages // Directement lié au LiveData du Repository
    // LiveData pour le message sélectionné.
    private val _selectedMessage = MutableLiveData<Message?>()
    val selectedMessage: LiveData<Message?> get() = _selectedMessage

    fun lastMessage(){
        repository.lastMessage()
    }

    fun ajouteMessage(date: Date, author: String, contenu: String) {
        repository.ajouteMessage(date, author, contenu)
    }

    fun supprimeMessage(id: Int) {
        repository.deleteMessageFromId(id)
    }

    fun selectMessage(message: Message?) {
        Log.d("MsgViewModel","Message sélectionné : "+message?.msg)
        _selectedMessage.value = message
    }
}