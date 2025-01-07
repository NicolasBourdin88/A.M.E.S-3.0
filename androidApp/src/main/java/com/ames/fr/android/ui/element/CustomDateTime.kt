package com.ames.fr.android.ui.element

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ames.fr.data.model.Event
import kotlinx.coroutines.delay

@Composable
fun CustomDateTime(event: Event) {
    val fontSize = event.fontSize!!.sp
    val xPercentage = event.x!!
    val yPercentage = event.y!!

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val adjustedX = xPercentage * screenWidth
    val adjustedY = yPercentage * screenHeight

    val currentTime = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            val formatter = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
            val date = java.util.Date()
            currentTime.value = formatter.format(date)
            delay(10000)
        }
    }

    Text(
        text = currentTime.value,
        fontSize = fontSize,
        color = Color.White,
        modifier = Modifier
            .offset(x = adjustedX.dp, y = adjustedY.dp)
    )
}
