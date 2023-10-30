package com.example.chatm1

import android.annotation.SuppressLint
import android.icu.util.LocaleData
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class MessagesRepo  private constructor() {

    companion object {
        @Volatile
        private var INSTANCE: MessagesRepo? = null

        fun getInstance(): MessagesRepo {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MessagesRepo().also { INSTANCE = it }
            }
        }
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages
    private val mListe = ArrayList<Message>()
    private var mAutoIncrement: Int = 0


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun lastMessage(){
        Log.d("REPO","LastMssage hors thread")
        thread {
            Log.d("REPO","LastMssage INTO thread")
            val url = URL("https://test.vautard.fr/creuse_srv")
            val connexion = url.openConnection()
            val dbf = DocumentBuilderFactory.newInstance()
            val db = dbf.newDocumentBuilder()
            val doc = db.parse(connexion.getInputStream())

            val enfants = doc.getElementsByTagName("CONTENT").item(0).childNodes
            for (enfant in 0..enfants.length){
                val id = enfants.item(enfant)
                val datesent = enfants.item(1).textContent
                val datet = SimpleDateFormat("yyyy-MMMM-dd, hh:mm:ss").parse(datesent)
                val author = enfants.item(2).textContent
                val msgs = enfants.item(3).textContent

                if (datet != null) {
                    ajouteMessage(datet, author, msgs)
                }

            }
        }
    }


    fun ajouteMessage(date: Date, author: String, msg: String) {
        mListe.add(Message(mAutoIncrement, author, msg, date))
        mAutoIncrement += 1
        _messages.value = mListe
    }

    operator fun get(i: Int): Message? {
        if (i<0 || i>= mListe.size)
            return null
        return mListe[i]
    }

    fun deleteMessageFromId(id: Int): Int {
        for (i in 0..mListe.size){
            if (mListe.get(i).id == id){
                mListe.removeAt(i)
                _messages.value = mListe
                return i
            }
        }

        return -1
    }

    fun size(): Int {
        return mListe.size
    }

    init {
        ajouteMessage(Calendar.getInstance().time,"Elfe", "Bonjour bonjour")
        ajouteMessage(Calendar.getInstance().time,"Ogre", "Broudaf, zog-zog ! ")
        ajouteMessage(Calendar.getInstance().time,"Voleur", "Salut à vous, belle compagnie ! Vous m'attendiez ?")
        ajouteMessage(Calendar.getInstance().time,"Toto", "On va faire un contenu un peu plus long, pour voir comment ça passe sur tous les affichages. Hopla ! Et même encore un peu plus long histoire de dire. Après tout, normalement on a tout un écran pour l'afficher, donc on est bien large...")
    }
}