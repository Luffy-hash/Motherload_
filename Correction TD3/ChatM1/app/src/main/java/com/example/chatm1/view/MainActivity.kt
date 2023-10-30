package com.example.chatm1.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.chatm1.MessageViewModel
import com.example.chatm1.R

class MainActivity : AppCompatActivity() {
    private lateinit var messageViewModel: MessageViewModel

    private var messagesFragmaent: MessagesFragment? = null
    private var messageDetailFragment: MessageDetailFragment? = null

    // Pour faire une mise à jour toutes les 15 secondes : on va donner un job toutes les
    //15 secondes au thread principal. On récupère son handler ici...
    private val handler = Handler(Looper.getMainLooper())
    // ... Et voici le runnable qu'on va lui donner. Il déclenche la mise à jour et se programme
    // lui-même à nouveau pour être exécuté dnas 15 secondes.
    private val updateRunnable = object : Runnable {
        override fun run() {
            messageViewModel.updateFromWebService()
            handler.postDelayed(this, 15000) // Appelle toutes les 15 secondes
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        messageDetailFragment = supportFragmentManager.findFragmentById(R.id.mainNoteDetailFrag) as MessageDetailFragment?
        messagesFragmaent = supportFragmentManager.findFragmentById(R.id.mainNoteFrag) as MessagesFragment?
        val addButton : Button = findViewById(R.id.button_add)
        val deleteButton : Button = findViewById(R.id.button_suppr)


        addButton.setOnClickListener {
            // C'est l'éctivity d'édition de message qui va se garger de l'ajout. On se contente de la lancer
            val intent = Intent(this, AjouteMessageActivity::class.java)
            startActivity(intent)
        }
        // Il n'y a pas de suppression des messages dans les webservices. On supprime donc cette fonctionnalité de l'app.
        deleteButton.visibility = View.GONE
/*
        deleteButton.setOnClickListener {
            if (messageViewModel.selectedMessage.value != null)
            {
                // Suppression du message (on passe par le viewModel : interdit de toucher au repository directement depuis la vue !)
                messageViewModel.supprimeMessage(messageViewModel.selectedMessage.value!!.id)
                // On déselectionne le message pour vider le MessageDetailFragment
                messageViewModel.selectMessage(null)
            }
        }
*/
    }

    override fun onResume() {
        super.onResume()
        // L'activity repasse en avant plan : on relance la mise à jour des messages
        handler.post(updateRunnable)
    }

    override fun onPause() {
        // L'activity passe en arrière-plan : on coupe la mise à jour des messages :
        // Pour ce faire, on vire de la file d'attente le job qui était posté.
        handler.removeCallbacks(updateRunnable)
        super.onPause()
    }
}