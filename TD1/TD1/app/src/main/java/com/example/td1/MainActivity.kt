package com.example.td1

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.td1.ui.theme.TD1Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TD1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   HelloScreen()
                    SecondScreen()
                }
            }
        }
    }
}

/**
 * First screen
 */
@Composable
fun HelloScreen(){

    Column (
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {

        /**
         * construction de notre image
         */
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "juste pour l'exemple",
            Modifier
                .padding(all = 1.6.dp)
                .weight(1f)
        )

        /**
         * button créer centre et aligner au centre
         */
        val context = LocalContext.current
        Button(onClick = {
            Log.i("DEBUG", "ce button est clicable")
            Toast.makeText(context, "Je marche bien!", Toast.LENGTH_SHORT).show()
        },
            modifier = Modifier
                .padding(10.dp)) {
            Text(text = "Hello World i'm button :)")
        }
    }
}

/**
 * Second screen
 */
@Composable
fun SecondScreen(modifier: Modifier = Modifier){
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "test du second écran" )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TD1Theme {
        HelloScreen()
        SecondScreen()
    }
}