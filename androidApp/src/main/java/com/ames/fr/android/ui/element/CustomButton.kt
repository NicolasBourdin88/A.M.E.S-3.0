package com.ames.fr.android.ui.element

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ames.fr.data.model.Button

@Composable
fun CustomButton(buttonData: Button, onClick: () -> Unit, onRefuse: () -> Unit) {
    val fileName = buttonData.fileName
    val xPercentage = buttonData.x
    val yPercentage = buttonData.y
    val originWidth = buttonData.originWidth
    val originHeight = buttonData.originHeight
    val isClickable = buttonData.isTappable

    val painter = runCatching { painterResource(id = drawableResourceId(fileName)) }.onFailure {
        Log.e("nicolas", "CustomButton - fileName: ${fileName}")
    }.getOrNull()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val adjustedX = xPercentage * screenWidth
    val adjustedY = yPercentage * screenHeight


    val modifier = if (fileName == "grille_25") {
        Modifier.fillMaxWidth()
    } else {
        Modifier.width(originWidth.dp)
    }
    Image(
        painter = painter!!,
        contentDescription = null,
        modifier = modifier
            .offset(
                x = adjustedX.dp, y = adjustedY.dp
            )
            .height(originHeight.dp)
            .then(
                if (isClickable) {
                    Modifier.clickable {
                        if (fileName == "no" || fileName == "refuse") {
                            onRefuse.invoke()
                        } else {
                            onClick.invoke()
                        }
                    }
                } else {
                    Modifier
                }
            ),
        contentScale = if (fileName == "grille_25") ContentScale.FillWidth else ContentScale.Fit
    )
}
