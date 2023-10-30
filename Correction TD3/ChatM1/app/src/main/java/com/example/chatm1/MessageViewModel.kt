package com.example.chatm1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatm1.model.Message
import com.example.chatm1.model.MessagesRepo

class MessageViewModel : ViewModel() {

    private val repository = MessagesRepo.getInstance()
    val messages: LiveData<List<Message>> = repository.messages // Directement lié au LiveData du Repository
    // LiveData pour le message sélectionné.
    private val _selectedMessage = MutableLiveData<Message?>()
    val selectedMessage: LiveData<Message?> get() = _selectedMessage

    // Dans cette version simple, les fonctions d'ajout, de suppression de message appellent
    // directement celles du repository. Ça sera un peu plus compliqué dans la feuille 2bis.
    fun ajouteMessage( author: String, contenu: String) {
        repository.ajouteMessage(author, contenu)
    }
    fun supprimeMessage(id: Int) {
        repository.deleteMessageFromId(id)
    }
    fun updateFromWebService() {
        repository.updateMessagesFromWebService()
    }

    // La sélection d'un message est du ressort strict du ViwModel. C'est donc lui qui gère tout.
    fun selectMessage(message: Message?) {
        _selectedMessage.value = message
        Log.d("MsgViewModel","Message sélectionné : "+message?.msg)
    }

}