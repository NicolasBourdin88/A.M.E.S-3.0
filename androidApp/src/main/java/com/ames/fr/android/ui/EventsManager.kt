package com.ames.fr.android.ui

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ames.fr.android.R
import com.ames.fr.android.ui.element.CustomButton
import com.ames.fr.android.ui.element.CustomText
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
                    TypeEvent.RM -> eventsToDisplay.removeIf { it.eventName == event.eventName }
                    else -> eventsToDisplay.add(event)
                }
                if (event.type == TypeEvent.WA) eventsToDisplay.add(event)
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
    val isOnClickContinueEnabled = remember { mutableStateOf(false) }
    Box(modifier = Modifier
        .fillMaxSize()
        .then(
            if (isOnClickContinueEnabled.value) {
                Modifier.clickable {
                    onShouldReadChange.invoke()
                    isOnClickContinueEnabled.value = false
                }
            } else {
                Modifier
            }
        )) {
        eventsToDisplay.forEach { event ->
            when (event.type) {
                TypeEvent.SP -> CustomButton(event, onClick = { onShouldReadChange.invoke() })
                TypeEvent.TC -> CustomText(event)
                TypeEvent.SO -> {}
                TypeEvent.AI -> {}
                TypeEvent.GM -> isOnClickContinueEnabled.value = true
                TypeEvent.RM -> {}
                TypeEvent.AT -> {
                    CustomText(event, isAnimated = true)
                }

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
}

private fun getAllEventsFromJson(resources: Resources): MutableList<Event> {
    val inputStream = resources.openRawResource(R.raw.script)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    val eventList = ScriptManager.getEventList(jsonString).toMutableList()
    return eventList
}
