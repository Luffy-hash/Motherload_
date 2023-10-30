package com.example.chatm1.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.chatm1.ChatM1Application
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.net.URLEncoder
import java.text.SimpleDateFormat
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList

class MessagesRepo  private constructor() {
    private val TAG = "MessagesRepo"
    private val BASE_URL = "https://test.vautard.fr/chatsrv/"

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
    // private var mAutoIncrement: Int = 0

    // La méthode d'ajout de message se contente désormais d'appeler le webservice.
    fun ajouteMessage(author: String, msg: String) {
        // On URLencode auteur et contenu du message pour s'assurer qu'ils pourront bien être passés
        // en GET au webservice (i.e. directement dnas l'url)
        val encodedAuthor = URLEncoder.encode(author, "UTF-8")
        val encodedMsg = URLEncoder.encode(msg, "UTF-8")
        val url = BASE_URL+"new_msg.php?author=$encodedAuthor&msg=$encodedMsg"

        // Requête réseau utilisant Volley : le WS est appelé et ce q'il a renvoyé est retourné sous
        // forme d'une chaîne de caractères
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG,"ajouteMessage: Message ajouté avec succès!")
                            // On appelle directement updateWebService pour mettre à jour
                            updateMessagesFromWebService()
                        } else {
                            Log.e(TAG, "ajouteMessage: Erreur - $status")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG,"Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG,"ajouteMessage error")
                error.printStackTrace()
            })

        ChatM1Application.instance.requestQueue?.add(stringRequest)
    }
    operator fun get(i: Int): Message? {
        if (i < 0 || i >= mListe.size) return null
        return mListe[i]
    }

    fun deleteMessageFromId(id: Int): Int {
        for (i in 0..mListe.size - 1) {
            if (mListe[i].id == id) {
                mListe.removeAt(i)
                _messages.value = mListe // Mise à jour du LiveData
                return i
            }
        }
        return -1
    }

    fun size(): Int {
        return mListe.size
    }

    fun updateMessagesFromWebService() {
        val url = BASE_URL+"last_msg.php"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d(TAG,"lasrmsg reponse")
                parseAndStoreXML(response)
            },
            { error ->
                Log.e(TAG,"lasrmsg error")
                error.printStackTrace()
            })

        // On utilise une RequestQueue commune à toute l'application.
        // Voir la doc de ChatM1Application pour voir comment ça se passe :-)
        ChatM1Application.instance.requestQueue?.add(stringRequest)
    }

    private fun parseAndStoreXML(xmlData: String) {
        val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
        // NB : on ne peut pas faire docBuilder.parse(xmlData) directement : cette méthode
        // s'attend à recevoir un nom de fichier, pas une string contenant du XML)
        val doc: Document = docBuilder.parse(xmlData.byteInputStream())

        // On vérifie le status
        val status = doc.getElementsByTagName("STATUS").item(0).textContent.trim()
        if (status != "OK")
        {
            Log.e(TAG,"Le WS répond ko : $status")
            return
        }

        val listElementsMessages = doc.getElementsByTagName("CONTENT").item(0).childNodes

        // On n'ajoutera que les messages qui ne sont pas déjà présents dans la liste.
        // On s'appuie sur le fait que la liste des messages est triée dans l'ordre croissant
        // quand on les récupère du webservice. (j'ai corrigé le WS en ce sens :D )
        val lastId = mListe.lastOrNull()?.id ?: -1

        for (i in 0 until listElementsMessages.length) {
            val node = listElementsMessages.item(i)
            // Normalement, on n'a pas d'autres nodes que des éléments xml, mais on n'est jamais
            // trop prudent...
            if (node.nodeType == Node.ELEMENT_NODE) {
                val elem = node as Element
                val id = elem.getElementsByTagName("id").item(0).textContent.toInt()

                if (id > lastId) {
                    Log.d(TAG,"Id = $id plus grand que $lastId")
                    val author = elem.getElementsByTagName("author").item(0).textContent
                    val dateStr = elem.getElementsByTagName("datesent").item(0).textContent
                    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr)
                    val msg = elem.getElementsByTagName("msg").item(0).textContent
                    mListe.add(Message(id, author, msg, date))
                }
            }
        }

        // Mise à jour de la LIveData.
        // NB : ça ne marche que parce qu'on est dans le thread principal. Si vous êtes dans un
        // thread secondaire, il faut faire  _messages.postValue(mListe)
        _messages.value = mListe
    }

}