package com.ames.fr.android.ui

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ames.fr.android.R
import com.ames.fr.data.ScriptManager
import com.ames.fr.data.model.Event
import com.ames.fr.data.model.TypeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _eventsToDisplayFlow = MutableStateFlow(mutableListOf<Event>())
    val eventsToDisplayFlow: StateFlow<List<Event>> = _eventsToDisplayFlow

    private val originalAllEvents: MutableList<Event> = getAllEventsFromJson(context.resources)
    private val remainingEvents: MutableList<Event> = originalAllEvents

    init {
        showNextEvents()
    }

    fun showNextEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val eventsToRemove = mutableListOf<Event>()
            val remainingEventsTemp = remainingEvents.toList()
            for (event in remainingEventsTemp) {
                eventsToRemove.add(event)

                when (event.type) {
                    TypeEvent.WA -> waitEvent(event)
                    TypeEvent.RM -> removeEventFromDisplayList(event)
                    TypeEvent.RMA -> clearCurrentEvents()
                    TypeEvent.SR -> break
                    else -> addEventToDisplayList(event)
                }
            }

            remainingEvents.removeAll(eventsToRemove)
        }
    }

    private suspend fun waitEvent(event: Event) {
        delay(event.duration!!.toLong() * 1000)
    }

    private fun clearCurrentEvents() {
        _eventsToDisplayFlow.value = mutableListOf()
    }

    private fun removeEventFromDisplayList(event: Event) {
        val list = _eventsToDisplayFlow.value.toMutableList()
        list.removeIf { it.eventName == event.eventName }
        _eventsToDisplayFlow.value = list
    }

    private fun addEventToDisplayList(event: Event) {
        val list = _eventsToDisplayFlow.value.toMutableList()
        list.add(event)
        _eventsToDisplayFlow.value = list
    }

    private fun getAllEventsFromJson(resources: Resources): MutableList<Event> {
        val inputStream = resources.openRawResource(R.raw.script)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return ScriptManager.getEventList(jsonString).toMutableList()
    }
}
