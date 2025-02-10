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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ames.fr.android.R
import com.ames.fr.data.model.Ghost
import kotlinx.coroutines.delay
import java.lang.Math.log1p
import kotlin.math.abs
import kotlin.math.ln1p
import kotlin.math.log10
import kotlin.math.sign

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

        AnimatedGhost(ghost)

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
fun AnimatedGhost(ghost: Ghost) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(SensorManager::class.java) }
    val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val frameIndex = remember { mutableStateOf(1) }

    // Référence initiale pour stabiliser le fantôme
    var initialAzimuth by remember { mutableStateOf<Float?>(null) }
    var initialPitch by remember { mutableStateOf<Float?>(null) }
    var initialRoll by remember { mutableStateOf<Float?>(null) }

    val sensorListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                        val rotationMatrix = FloatArray(9)
                        val orientationValues = FloatArray(3)

                        SensorManager.getRotationMatrixFromVector(rotationMatrix, it.values)
                        SensorManager.getOrientation(rotationMatrix, orientationValues)

                        val azimuth = Math.toDegrees(orientationValues[0].toDouble()).toFloat()  // Rotation horizontale
                        val pitch = Math.toDegrees(orientationValues[1].toDouble()).toFloat()   // Inclinaison haut/bas
                        val roll = Math.toDegrees(orientationValues[2].toDouble()).toFloat()    // Inclinaison latérale

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
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    LaunchedEffect(Unit) {
        rotationSensor?.let { sensorManager?.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_UI) }

        while (true) {
            delay((ghost.speed * 1000).toLong() / ghost.numberOfImages)
            frameIndex.value = (frameIndex.value % ghost.numberOfImages) + 1
        }
    }

    val imageName = "${ghost.ghostImageFileName}_${frameIndex.value}"
    val ghostImage = painterResource(id = drawableResourceId(imageName))

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = ghostImage,
            contentDescription = "Fantôme animé",
            modifier = Modifier
                .size(200.dp)
                .offset(x = with(LocalDensity.current) { offsetX.dp }, y = with(LocalDensity.current) { offsetY.dp })
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
