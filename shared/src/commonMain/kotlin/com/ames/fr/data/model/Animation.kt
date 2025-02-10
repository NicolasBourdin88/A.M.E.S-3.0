package com.ames.fr.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Animation(
    val fileName: String,
    val nbImage: Int,
    val x: Float,
    val y: Float,
    @SerialName("loop")
    val isLoop: Boolean,
    val frameRate: Float,
    val imageWidth: Float,
    val imageHeight: Float,
    val contentScale: ContentScale
) {
    companion object {
        @Serializable
        enum class ContentScale {
            @SerialName("fullScreen")
            FULL_SCREEN,

            @SerialName("imageSize")
            IMAGE_SIZE
        }
    }
}
