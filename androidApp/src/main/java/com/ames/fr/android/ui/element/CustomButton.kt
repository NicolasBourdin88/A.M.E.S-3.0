package com.ames.fr.android.ui.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ames.fr.data.model.Button

@Composable
fun CustomButton(buttonData: Button, onClick: () -> Unit) {
    val fileName = buttonData.fileName
    val xPercentage = buttonData.x
    val yPercentage = buttonData.y
    val originWidth = buttonData.originWidth
    val originHeight = buttonData.originHeight
    val isClickable = buttonData.isTappable

    val painter = painterResource(id = drawableResourceId(fileName))

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val adjustedX = xPercentage * screenWidth
    val adjustedY = yPercentage * screenHeight

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
