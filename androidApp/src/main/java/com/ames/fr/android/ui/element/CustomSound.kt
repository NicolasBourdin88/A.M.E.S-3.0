package com.ames.fr.android.ui.element

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ames.fr.data.model.Event

@Composable
fun CustomSound(event: Event) {

    Log.e("nicolas", "CustomSound - sound ${event.fileName!!}")

    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    LaunchedEffect(event.fileName) {
        val rawResId = getRawResIdByName(context, event.fileName!!)
        if (rawResId != null) {
            mediaPlayer.setDataSource(context.resources.openRawResourceFd(rawResId).fileDescriptor)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }
}

fun getRawResIdByName(context: Context, fileName: String): Int? {
    val resId = context.resources.getIdentifier(fileName, "raw", context.packageName)
    return if (resId != 0) resId else null
}
