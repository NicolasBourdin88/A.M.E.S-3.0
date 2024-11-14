package com.ames.fr.android.ui.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ames.fr.data.model.Event

@Composable
fun CustomButton(event: Event, onClick: () -> Unit) {
    val fileName = event.fileName!!
    val x = event.x!!
    val y = event.y!!
    val originWidth = event.originWidth!!
    val originHeight = event.originHeight!!
    val isClickable = event.isTappable!!

    val painter = painterResource(id = drawableResourceId(fileName))

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val adjustedX = (x / 1000f) * screenWidth
    val adjustedY = (y / 1000f) * screenHeight


    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .offset(
                x = adjustedX.dp, y = adjustedY.dp
            )
            .width(originWidth.dp)
            .height(originHeight.dp)
            .then(
                if (isClickable) Modifier.clickable {
                    onClick.invoke()
                }
                else Modifier
            ),
    )
}

@Composable
fun drawableResourceId(name: String): Int {
    return LocalContext.current.resources.getIdentifier(
        name,
        "drawable",
        LocalContext.current.packageName
    )
}
