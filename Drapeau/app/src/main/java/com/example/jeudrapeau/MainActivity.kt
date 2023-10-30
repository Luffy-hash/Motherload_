package com.example.jeudrapeau

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.jeudrapeau.ui.theme.JeuDrapeauTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JeuDrapeauTheme {
                MaterialTheme{

                }
            }
        }
    }

    data class Drapeau(val drapeau: Int, val nameDrapeau: String)

    @Composable
    fun AfficheDrapeau(drap: Int, drapName: String){
        Row {
            Column {

            }
        }
    }

    @Composable
    fun RecupDrapeau(drapeaux: Drapeau){
        LazyColumn{
            items(drapeaux){
                    drapeau -> AfficheDrapeau(drapeau)
            }
        }
    }


}
