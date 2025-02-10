package com.ames.fr.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovableImage(
    val xBegin: Float,
    val yBegin: Float,
    val xEnd: Float,
    val yEnd: Float,
    val fileName: String,
    val originWidth: Float,
    val originHeight: Float,
    @SerialName("loop")
    val isLoop: Boolean,
)
