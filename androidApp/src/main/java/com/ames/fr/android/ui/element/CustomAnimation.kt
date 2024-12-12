package com.ames.fr.android.ui.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.ames.fr.data.model.Event
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.delay
import com.ames.fr.data.model.ContentScale as ContentScaleEvent

@Composable
fun CustomAnimation(event: Event) {
    val fileName = remember { mutableStateOf("${event.fileName!!}_1") }
    val numberOfImages = event.nbImage!!
    val originWidth = event.imageWidth!!
    val originHeight = event.imageHeight!!
    val xPercentage = event.x!!
    val yPercentage = event.y!!

    val contentScaleEvent = event.contentScale!!
    val shouldChangeSize = contentScaleEvent == ContentScaleEvent.IMAGE_SIZE
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val adjustedX = xPercentage * screenWidth
    val adjustedY = yPercentage * screenHeight

    val modifier = if (shouldChangeSize) {
        Modifier
            .height(originHeight.dp)
            .width(originWidth.dp)
    } else {
        Modifier.fillMaxWidth()
    }

    LaunchedEffect(Unit) {
        while (true) {
            repeat(numberOfImages) {
                delay((event.frameRate!! * 1000).toLong())
                fileName.value = "${event.fileName!!}_$it"
            }
        }
    }

    Image(
        painter = rememberDrawablePainter(
            drawable = getDrawable(LocalContext.current, drawableResourceId(fileName.value))
        ),
        contentDescription = "Loading animation",
        modifier = modifier.offset(x = adjustedX.dp, y = adjustedY.dp),
        contentScale = ContentScale.FillWidth,
    )
}
