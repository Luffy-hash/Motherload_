package com.example.chatm1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import java.util.Calendar

class MainActivity2 : AppCompatActivity() {

    private lateinit var messageViewModel: MessageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        messageViewModel = ViewModelProvider(this)[MessageViewModel::class.java]

        val btnConfirm: Button = findViewById(R.id.buttonConfirm)
        val textTitle: EditText = findViewById(R.id.editTextTitle)
        val textAuthor: EditText = findViewById(R.id.editTextAuthor)


        btnConfirm.setOnClickListener(View.OnClickListener {
            messageViewModel.ajouteMessage(Calendar.getInstance().time,
                textAuthor.text.toString(), textTitle.text.toString())
            finish()
        })


    }

}