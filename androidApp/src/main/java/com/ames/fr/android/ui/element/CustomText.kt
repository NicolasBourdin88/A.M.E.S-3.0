package com.ames.fr.android.ui.element

import android.util.Log
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ames.fr.data.model.Event
import kotlinx.coroutines.delay

@Composable
fun CustomText(event: Event, isAnimated: Boolean = false) {
    val fontSize = event.fontSize!!.sp
    val originalText = event.text!!
    val xPercentage = event.x!!
    val yPercentage = event.y!!

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val adjustedX = xPercentage * screenWidth
    val adjustedY = yPercentage * screenHeight

    val textToDisplay = remember { mutableStateOf(if (isAnimated) "" else originalText) }
    val paddingStartToAdd = if (adjustedX < 10) 40.dp else 0.dp
    val paddingTopToAdd = if (adjustedY < 10) 40.dp else 0.dp

    LaunchedEffect(isAnimated) {
        if (isAnimated) {
            textToDisplay.value = ""
            for (i in originalText.indices) {
                delay((event.printSpeed!! * 1000).toLong())
                textToDisplay.value += originalText[i]
            }
        }
    }

    Text(
        text = textToDisplay.value,
        fontSize = fontSize,
        color = Color.White,
        modifier = Modifier
            .offset(x = adjustedX.dp, y = adjustedY.dp)
            .padding(start = paddingStartToAdd, top = paddingTopToAdd)
    )
}
