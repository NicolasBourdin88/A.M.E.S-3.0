package com.ames.fr.android.ui.element

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import com.ames.fr.data.model.Event


@Composable
fun CustomSound(event: Event, assets: AssetManager) {
    fun playSound(assets: AssetManager, fileName: String) {
        var mediaPlayer = MediaPlayer()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
            mediaPlayer = MediaPlayer()
        }
        val descriptor: AssetFileDescriptor = assets.openFd("$fileName.mp3")
        mediaPlayer.setDataSource(
            descriptor.fileDescriptor,
            descriptor.startOffset,
            descriptor.length
        )
        descriptor.close()

        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    playSound(assets, event.fileName!!)
}
