package com.example.chatm1.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.chatm1.MessageViewModel
import com.example.chatm1.R

class AjouteMessageActivity : AppCompatActivity() {

    // On se donne le même ViewModel que pour la MainActivity. On aurait pu en créer un autre plus
    // simple se contentant de permettre d'ajouter un message.
    private lateinit var messageViewModel: MessageViewModel
    private lateinit var editTextAuthor:EditText
    private lateinit var editTextMessage:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajoute_message)
        // On n'oublie pas d'initialiser le vieewModel pour cette activity
        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        // On récupère les deux EditText (on va les lire quand on enverra le message)
        editTextAuthor = findViewById(R.id.edit_auteur)
        editTextMessage = findViewById(R.id.edit_message)

        val buttonSend : Button = findViewById(R.id.button_send)
        buttonSend.setOnClickListener {
            // On récupère le contenu des EditText et on les utilise pour le nouveau message
            val author = editTextAuthor.text.toString()
            val message = editTextMessage.text.toString()

            // on le stock d'abord sur le share preference
            stockageSharePref(author, message);

            messageViewModel.ajouteMessage(author,message)
            // Une fois le message ajouté, on peut quitter l'activity. Cela reviendra automatiquement
            // à l'actvity précédente, c'est à dire MainActivity.
            finish()
        }
    }


    // developpement du sharePref
    fun stockageSharePref (author: String, msg: String){
        val preference = getSharedPreferences("myStockage", MODE_PRIVATE);
        val preferens = preference.edit();

        preferens.putString(author, msg);
        preferens.apply();

    }


}