package com.ames.fr.android.ui.element

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.ames.fr.data.model.Event

@Composable
fun CustomSound(event: Event, assets: AssetManager) {
    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(event.fileName) {
        fun playSound() {
            runCatching {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                }

                val descriptor: AssetFileDescriptor = assets.openFd("${event.fileName}.mp3")
                mediaPlayer.setDataSource(
                    descriptor.fileDescriptor,
                    descriptor.startOffset,
                    descriptor.length
                )
                descriptor.close()

                mediaPlayer.setOnPreparedListener {
                    it.start()
                }

                mediaPlayer.isLooping = event.isLoop ?: false
                mediaPlayer.prepareAsync()
            }.onFailure {
                Log.e("CustomSound", "Song failed to read : ${it.message}")
            }
        }

        playSound()

        onDispose {
            runCatching {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }
                mediaPlayer.reset()
                mediaPlayer.release()
            }.onFailure {
                Log.e("CustomSound", "Song failed to liberate ${it.message}")

            }
        }
    }
}
