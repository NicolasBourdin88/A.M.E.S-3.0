package com.ames.fr.android.ui.element

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.ames.fr.data.model.Sound

@Composable
fun CustomSound(dataSound: Sound, assets: AssetManager) {
    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(dataSound.fileName) {
        fun playSound() {
            runCatching {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                }

                val descriptor: AssetFileDescriptor = assets.openFd("${dataSound.fileName}.mp3")
                mediaPlayer.setDataSource(
                    descriptor.fileDescriptor,
                    descriptor.startOffset,
                    descriptor.length
                )
                descriptor.close()

                mediaPlayer.setOnPreparedListener {
                    it.start()
                }

                mediaPlayer.isLooping = dataSound.isLoop
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
