package com.ames.fr.android.ui

import android.content.res.AssetManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ames.fr.android.ui.element.CustomAnimation
import com.ames.fr.android.ui.element.CustomButton
import com.ames.fr.android.ui.element.CustomCamera
import com.ames.fr.android.ui.element.CustomMovingImage
import com.ames.fr.android.ui.element.CustomSound
import com.ames.fr.android.ui.element.CustomText
import com.ames.fr.data.model.Event
import com.ames.fr.data.model.TypeEvent

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(clickableModifier)
    ) {
        eventsToDisplay.sortEvents().forEach { event ->
            when (event.type) {
                TypeEvent.CA -> CustomCamera(isTorchLightEnabled.value)
                TypeEvent.SP -> CustomButton(event, onClick = { eventViewModel.showNextEvents() })
                TypeEvent.TC -> CustomText(event)
                TypeEvent.SO -> CustomSound(event, assets)
                TypeEvent.AI -> CustomAnimation(event)
                TypeEvent.GM -> isOnClickContinueEnabled.value = true
                TypeEvent.AT -> CustomText(event, isAnimated = true)
                TypeEvent.MI -> CustomMovingImage(event)
                TypeEvent.DT -> {}
                TypeEvent.TL -> isTorchLightEnabled.value = event.isTorchLightActivated == true
                else -> {}
            }
        }
    }
}

private fun List<Event>.sortEvents(): List<Event> {
    return this.sortedBy { event ->
        if (event.type == TypeEvent.CA) 0 else 1
    }
}
