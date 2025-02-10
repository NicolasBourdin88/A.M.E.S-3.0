package com.ames.fr.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Button(
    val x: Float,
    val y: Float,
    val fileName: String,
    val originWidth: Float,
    val originHeight: Float,
    val isTappable: Boolean,
)
