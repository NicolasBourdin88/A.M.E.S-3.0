package com.ames.fr.android.ui.element

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ames.fr.data.model.Event
import kotlinx.coroutines.delay

@Composable
fun CustomMovingImage(event: Event) {
    val fileName = event.fileName!!
    val xStartPercentage = event.xBegin!!
    val yStartPercentage = event.yBegin!!
    val xEndPercentage = event.xEnd!!
    val yEndPercentage = event.yEnd!!
    val originWidth = event.originWidth!!
    val originHeight = event.originHeight!!
    val isLoop = event.isLoop!!

    val painter = painterResource(id = drawableResourceId(fileName))

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val startX = xStartPercentage * screenWidth
    val startY = yStartPercentage * screenHeight
    val endY = yEndPercentage * screenHeight
    val endX = xEndPercentage * screenWidth

    var moved by remember { mutableStateOf(false) }

    val animatedY by animateFloatAsState(
        targetValue = if (moved) endY else startY,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
    )

    val animatedX by animateFloatAsState(
        targetValue = if (moved) endX else startX,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .offset(x = animatedX.dp, y = animatedY.dp)
            .width(originWidth.dp)
            .height(originHeight.dp)
    )

    LaunchedEffect(Unit) {
        moved = true
        while (isLoop) {
            delay(2000)
            if (animatedY == endY && animatedX == endX && moved) {
                moved = false
            }
            if (animatedY == startY && animatedX == startX && !moved) {
                moved = true
            }
        }
    }
}
