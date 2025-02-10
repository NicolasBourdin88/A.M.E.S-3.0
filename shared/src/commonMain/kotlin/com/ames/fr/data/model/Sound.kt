package com.ames.fr.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sound(
    val fileName: String,
    @SerialName("loop")
    val isLoop: Boolean,
)
