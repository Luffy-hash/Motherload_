package com.example.chatm1

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var messageViewModel: MessageViewModel

    private var messagesFragmaent: MessagesFragment? = null
    private var messageDetailFragment: MessageDetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActiity","OnCreate !")
        setContentView(R.layout.activity_main)
        messageViewModel = ViewModelProvider(this)[MessageViewModel::class.java]
        messageDetailFragment = supportFragmentManager.findFragmentById(R.id.mainNoteDetailFrag) as MessageDetailFragment?
        messagesFragmaent = supportFragmentManager.findFragmentById(R.id.mainNoteFrag) as MessagesFragment?



        val buttonAjout : Button = findViewById(R.id.AjoutBoutton)
        val buttonSupp : Button = findViewById(R.id.SuppBoutton)

        buttonAjout.setOnClickListener(View.OnClickListener {
            val ajoutMsg:Intent = Intent(this, MainActivity2::class.java)
            startActivity(ajoutMsg)
        })

        buttonSupp.setOnClickListener {
            messageViewModel.supprimeMessage(messageViewModel.selectedMessage.value!!.id)
            messageViewModel.selectedMessage.value!!.msg = ""
        }

        // execution sur un autre thread
        messageViewModel.lastMessage()
    }


}

