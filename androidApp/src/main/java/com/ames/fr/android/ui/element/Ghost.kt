package com.ames.fr.android.ui.element

import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.ames.fr.android.R
import com.ames.fr.data.model.Ghost
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.delay

const val crossHairWidth = 150

@Composable
fun Ghost(ghost: Ghost, onGhostHit: () -> Unit) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(SensorManager::class.java) }

    var chargeLoad by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            if (chargeLoad + 1 == 7) {
                chargeLoad -= 4
            } else {
                chargeLoad += 1
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AnimationGhost(ghost)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CrossHair()
            Spacer(modifier = Modifier.height(2.dp))
            ChargeBar(chargeLoad)
        }
    }

    ButtonExorcist({
        // See if ghost in crosshair if yes call onGhostHit
    })
}

@Composable
fun AnimationGhost(ghost: Ghost) {

    val fileName = remember { mutableStateOf("${ghost.ghostImageFileName}_1") }
    val numberOfImages = ghost.numberOfImages
    val xPercentage = 0.4
    val yPercentage = 0.4

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val adjustedX = xPercentage * screenWidth
    val adjustedY = yPercentage * screenHeight

    LaunchedEffect(Unit) {
        while (true) {
            repeat(numberOfImages - 1) {
                delay((ghost.speed * 1000).toLong())
                fileName.value = "${ghost.ghostImageFileName}_${it + 1}"
            }
        }
    }

    Image(
        painter = rememberDrawablePainter(
            drawable = getDrawable(LocalContext.current, drawableResourceId(fileName.value))
        ),
        contentDescription = "Loading animation",
        modifier = Modifier
            .size(200.dp)
            .offset(x = adjustedX.dp, y = adjustedY.dp),
        contentScale = androidx.compose.ui.layout.ContentScale.FillWidth,
    )
}

@Composable
fun ChargeBar(chargeLoad: Int = 1) {
    Row(
        modifier = Modifier
            .width(crossHairWidth.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(6) { index ->
            val isColored = index < chargeLoad
            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(if (isColored) Color.Red else Color.Transparent)
                    .border(5.dp, if (isColored) Color.Transparent else Color.Transparent)
            )
        }
    }
}

@Composable
fun CrossHair() {
    Canvas(modifier = Modifier.size(crossHairWidth.dp)) {
        val strokeWidth = 4f
        drawRect(
            color = Color.Red,
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
fun ButtonExorcist(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.exorcise),
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.BottomStart)
                .clickable { onClick.invoke() }
        )
    }
}

@Preview
@Composable
private fun PreviewGhost() {
    var chargeLoad by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            if (chargeLoad + 1 == 7) {
                chargeLoad -= 4
            } else {
                chargeLoad += 1
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CrossHair()
        Spacer(modifier = Modifier.height(2.dp))
        ChargeBar(chargeLoad)
    }
}
