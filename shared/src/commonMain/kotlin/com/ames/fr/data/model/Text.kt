package com.ames.fr.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Text(
    val x: Float,
    val y: Float,
    val text: String,
    val fontSize: Float,
    val printSpeed: Float? = null,
)
