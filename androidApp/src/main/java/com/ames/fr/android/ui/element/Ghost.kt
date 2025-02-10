package com.ames.fr.android.ui.element

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.ames.fr.android.R
import com.ames.fr.data.model.Ghost
import kotlinx.coroutines.delay
import kotlin.math.abs

const val crossHairWidth = 150
const val ghostSize = 200
const val maxChargeLoad = 6

@Composable
fun Ghost(ghost: Ghost, onGhostHit: () -> Unit) {
    var chargeLoad by remember { mutableIntStateOf(0) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var sizeMultiplication = 1f

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            if (chargeLoad < maxChargeLoad) {
                chargeLoad++
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AnimatedGhost(ghost, onUpdatePosition = { newX, newY, newSizeMultiplication ->
            offsetX = newX
            offsetY = newY
            sizeMultiplication = newSizeMultiplication
        })

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

    val density = LocalDensity.current

    ButtonExorcist(onClick = {
        if (chargeLoad >= 4) {
            chargeLoad -= 4
            if (isGhostInCrossHair(
                    offsetX,
                    offsetY,
                    density,
                    sizeMultiplication
                )
            ) onGhostHit.invoke()
        }
    })

}


private fun isGhostInCrossHair(
    offsetX: Float,
    offsetY: Float,
    density: Density,
    sizeMultiplication: Float,
): Boolean {
    val ghostSizePx = with(density) { (ghostSize * sizeMultiplication).dp.toPx() }
    val crossHairWidthPx = with(density) { crossHairWidth.dp.toPx() }

    val ghostLeft = offsetX - ghostSizePx / 2
    val ghostRight = offsetX + ghostSizePx / 2
    val ghostTop = offsetY - ghostSizePx / 2
    val ghostBottom = offsetY + ghostSizePx / 2

    val crossHairLeft = -crossHairWidthPx / 2
    val crossHairRight = crossHairWidthPx / 2
    val crossHairTop = -crossHairWidthPx / 2
    val crossHairBottom = crossHairWidthPx / 2



    return abs(ghostLeft) > abs(crossHairLeft) &&
            abs(ghostRight) > abs(crossHairRight) &&
            abs(ghostTop) > abs(crossHairTop) &&
            abs(ghostBottom) > abs(crossHairBottom)
}


@Composable
fun AnimatedGhost(
    ghost: Ghost,
    onUpdatePosition: (Float, Float, Float) -> Unit
) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(SensorManager::class.java) }
    val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val frameIndex = remember { mutableIntStateOf(1) }
    var sizeMultiplicationGhost by remember { mutableFloatStateOf(1f) }

    var initialAzimuth by remember { mutableStateOf<Float?>(null) }
    var initialPitch by remember { mutableStateOf<Float?>(null) }
    var initialRoll by remember { mutableStateOf<Float?>(null) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(30)
            sizeMultiplicationGhost += 0.01F
        }
    }

    val sensorListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                        val rotationMatrix = FloatArray(9)
                        val orientationValues = FloatArray(3)

                        SensorManager.getRotationMatrixFromVector(rotationMatrix, it.values)
                        SensorManager.getOrientation(rotationMatrix, orientationValues)

                        val azimuth = Math.toDegrees(orientationValues[0].toDouble())
                            .toFloat()  // Rotation horizontale
                        val pitch = Math.toDegrees(orientationValues[1].toDouble())
                            .toFloat()   // Inclinaison haut/bas
                        val roll = Math.toDegrees(orientationValues[2].toDouble())
                            .toFloat()    // Inclinaison latérale

                        if (initialAzimuth == null) {
                            initialAzimuth = azimuth
                            initialPitch = pitch
                            initialRoll = roll
                        }

                        // Déplacement X basé sur la rotation horizontale
                        offsetX = (initialAzimuth!! - azimuth) * 20f  // Facteur d'ajustement

                        // Déplacement Y basé sur le pitch et le roll
                        val pitchDiff = initialPitch!! - pitch
                        val rollDiff = initialRoll!! - roll

                        offsetY = (pitchDiff + rollDiff) * 15f  // Ajustement de l'échelle pour le Y

                        onUpdatePosition(offsetX, offsetY, sizeMultiplicationGhost)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    LaunchedEffect(Unit) {
        rotationSensor?.let {
            sensorManager.registerListener(
                sensorListener,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        while (true) {
            delay((ghost.speed * 1000).toLong())
            frameIndex.intValue = (frameIndex. intValue % ghost.numberOfImages) + 1
        }
    }

    val imageName = "${ghost.ghostImageFileName}_${frameIndex.value}"
    val ghostImage = painterResource(id = drawableResourceId(imageName))

    val density = LocalDensity.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = ghostImage,
            contentDescription = null,
            modifier = Modifier
                .width((ghostSize * sizeMultiplicationGhost).dp)
                .aspectRatio(ghostImage.intrinsicSize.width / ghostImage.intrinsicSize.height)  // Conserver le ratio original
                .offset(x = with(density) { offsetX.dp }, y = with(density) { offsetY.dp }),
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
fun ChargeBar(chargeLoad: Int = 1) {
    Row(
        modifier = Modifier
            .width(crossHairWidth.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(maxChargeLoad) { index ->
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
