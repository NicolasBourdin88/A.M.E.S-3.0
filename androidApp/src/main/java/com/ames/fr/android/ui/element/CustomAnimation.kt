package com.ames.fr.android.ui.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getDrawable
import com.ames.fr.data.model.Event
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.delay

@Composable
fun CustomAnimation(event: Event) {
    val fileName = remember { mutableStateOf("${event.fileName!!}_1") }
    val numberOfImages = event.nbImage!!

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
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
    )
}
