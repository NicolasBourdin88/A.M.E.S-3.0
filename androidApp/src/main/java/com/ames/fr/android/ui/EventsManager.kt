package com.ames.fr.android.ui

import android.content.res.Resources
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.ames.fr.android.R
import com.ames.fr.android.ui.element.CustomButton
import com.ames.fr.data.ScriptManager
import com.ames.fr.data.model.Event
import com.ames.fr.data.model.TypeEvent
import kotlinx.coroutines.delay

@Composable
fun EventsManager(resources: Resources) {
    val eventList = remember { getAllEventsFromJson(resources) }
    val shouldRead = remember { mutableStateOf(true) }

    val eventsToDisplay = remember { mutableStateListOf<Event>() }
    LaunchedEffect(shouldRead.value) {
        if (shouldRead.value) {
            for (event in eventList) {
                when (event.type) {
                    TypeEvent.WA -> delay(event.duration!!.toLong() * 1000)
                    TypeEvent.SR -> break
                    else -> eventsToDisplay.add(event)
                }
            }

            eventList.removeAll(eventsToDisplay)
            eventList.removeAt(0)
            shouldRead.value = false
        }
    }

    DisplayEvents(
        eventsToDisplay = eventsToDisplay,
        onShouldReadChange = {
            shouldRead.value = true
        }, onRemoveAllAction = {
            val listToRemove = eventsToDisplay.takeWhile { it.type != TypeEvent.RMA }
            eventsToDisplay.removeAll(listToRemove)
        }
    )
}

@Composable
private fun DisplayEvents(
    eventsToDisplay: List<Event>,
    onShouldReadChange: () -> Unit,
    onRemoveAllAction: () -> Unit,
) {
    eventsToDisplay.forEach { event ->
        when (event.type) {
            TypeEvent.SP -> CustomButton(event, onClick = { onShouldReadChange.invoke() })
            TypeEvent.TC -> Text(event.text!!)
            TypeEvent.SO -> {}
            TypeEvent.AI -> {}
            TypeEvent.GM -> {}
            TypeEvent.RM -> {}
            TypeEvent.AT -> Text(event.text!!)
            TypeEvent.MI -> {}
            TypeEvent.STS -> {}
            TypeEvent.CA -> {}
            TypeEvent.DT -> {}
            TypeEvent.TL -> {}
            TypeEvent.RMA -> onRemoveAllAction.invoke()
            else -> {}
        }
    }
}

private fun getAllEventsFromJson(resources: Resources): MutableList<Event> {
    val inputStream = resources.openRawResource(R.raw.script)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    val eventList = ScriptManager.getEventList(jsonString).toMutableList()
    return eventList
}
