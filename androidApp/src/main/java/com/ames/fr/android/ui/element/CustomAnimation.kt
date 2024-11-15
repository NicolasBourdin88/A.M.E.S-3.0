package com.ames.fr.android.ui.element

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getDrawable
import com.ames.fr.data.model.Event
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CustomAnimation(event: Event) {
//    val gifEnabledLoader = ImageLoader.Builder(context)
//        .components {
//            if (SDK_INT >= 28) {
//                add(ImageDecoderDecoder.Factory())
//            } else {
//                add(GifDecoder.Factory())
//            }
//        }.build()
    val fileName = event.fileName!!
    val xPercentage = event.x!!
    val yPercentage = event.y!!


    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val adjustedX = xPercentage * screenWidth
    val adjustedY = yPercentage * screenHeight

    val imageWidth = event.imageWidth!! * screenWidth
    val imageHeight = event.imageHeight!! * screenHeight

    Image(
        painter = rememberDrawablePainter(
            drawable = getDrawable(
                LocalContext.current,
                drawableResourceId(fileName),
            )
        ),
        contentDescription = "Loading animation",
        contentScale = ContentScale.FillWidth,
    )
}
