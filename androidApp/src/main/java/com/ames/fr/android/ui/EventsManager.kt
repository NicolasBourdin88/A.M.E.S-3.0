package com.ames.fr.android.ui

import android.content.res.AssetManager
import android.hardware.SensorManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ames.fr.android.ui.element.CustomAnimation
import com.ames.fr.android.ui.element.CustomButton
import com.ames.fr.android.ui.element.CustomCamera
import com.ames.fr.android.ui.element.CustomDateTime
import com.ames.fr.android.ui.element.CustomMovingImage
import com.ames.fr.android.ui.element.CustomSound
import com.ames.fr.android.ui.element.CustomText
import com.ames.fr.android.ui.element.Ghost
import com.ames.fr.android.ui.element.Scan
import com.ames.fr.data.model.Event
import com.ames.fr.data.model.Event.Companion.TypeEvent
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun EventsManager(assets: AssetManager, eventViewModel: EventViewModel = hiltViewModel()) {
    val isOnClickContinueEnabled = remember { mutableStateOf(false) }
    val isTorchLightEnabled = remember { mutableStateOf(false) }
    val eventsToDisplay by eventViewModel.eventsToDisplayFlow.collectAsState(initial = emptyList())

    val clickableModifier = if (isOnClickContinueEnabled.value) {
        Modifier.clickable {
            eventViewModel.showNextEvents()
            isOnClickContinueEnabled.value = false
        }
    } else {
        Modifier
    }

    Box(modifier = Modifier.fillMaxSize().then(clickableModifier)) {
        eventsToDisplay.sortEvents().forEach { event ->
            key(event.uuid) {
                when (event.type) {
                    TypeEvent.CA -> CustomCamera(isTorchLightEnabled.value)
                    TypeEvent.SP -> CustomButton(event.button!!, onClick = { eventViewModel.showNextEvents() })
                    TypeEvent.TC -> CustomText(event.text!!)
                    TypeEvent.SO -> CustomSound(event.sound!!, assets)
                    TypeEvent.AI -> CustomAnimation(event.animation!!)
                    TypeEvent.GM -> isOnClickContinueEnabled.value = true
                    TypeEvent.AT -> CustomText(event.text!!, isAnimated = true)
                    TypeEvent.MI -> CustomMovingImage(event.movableImage!!)
                    TypeEvent.DT -> CustomDateTime(event.dateTime!!)
                    TypeEvent.TL -> isTorchLightEnabled.value = event.torchLight!!.isActivated == true
                    TypeEvent.SC -> Scan(onClick = { eventViewModel.showNextEvents() })
                    TypeEvent.GH -> Ghost(event.ghost!!, onGhostHit = { /* TODO JE GERE ICI TKT */ })
                    else -> {}
                }
            }
        }
    }
}

private fun List<Event>.sortEvents(): List<Event> {
    return this.sortedBy { event ->
        if (event.type == TypeEvent.CA) 0 else 1
    }
}
