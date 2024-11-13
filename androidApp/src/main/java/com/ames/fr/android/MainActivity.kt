package com.ames.fr.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ames.fr.android.ui.theme.MyApplicationTheme
import com.ames.fr.data.GameManager
import com.ames.fr.data.model.Event
import com.ames.fr.data.model.TypeEvent
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inputStream = resources.openRawResource(R.raw.script)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val eventList = GameManager.getEventList(jsonString)

        setContent {
            val currentEvent = remember {
                mutableStateOf(eventList.first() to 1)
            }

            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(Unit) {
                        eventList.forEachIndexed { position, event ->
                            currentEvent.value = event to position
                            delay(200)
                        }
                    }
                    currentEvent.value.first.DisplayEvent()
                }
            }
        }
    }
}

@Composable
fun Event.DisplayEvent() {
    when (this.type) {
        TypeEvent.SP -> Text("SP")
        TypeEvent.TC -> Text("TC")
        TypeEvent.SR -> Text("SR")
        TypeEvent.RMA -> Text("RMA")
        TypeEvent.SO -> Text("SO")
        TypeEvent.AI -> Text("AI")
        TypeEvent.WA -> Text("WA")
        TypeEvent.GM -> Text("GM")
        TypeEvent.RM -> Text("RM")
        TypeEvent.AT -> Text("AT")
        TypeEvent.MI -> Text("MI")
        TypeEvent.STS -> Text("STS")
        TypeEvent.CA -> Text("CA")
        TypeEvent.DT -> Text("DT")
        TypeEvent.TL -> Text("TL")
    }
}
