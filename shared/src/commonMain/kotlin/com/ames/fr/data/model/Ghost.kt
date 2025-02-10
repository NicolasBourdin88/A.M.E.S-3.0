package com.ames.fr.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Ghost(
    val numberOfImages: Int,
    val ghostSoundFileName: String,
    val numberOfShoots: Int,
    val ghostImageFileName: String,
    val animationDuration: Int,
    val movementDuration: Int,
    val movementIsLooped: Boolean,
    val xTolerance: Double,
    val yTolerance: Double,
    val ghostPhi: Int,
    val ghostTeta: Int,
    val ghostDistance: Int,
    val scaleFactor: Int,
    val speed: Double,
    val updateRate: Int
)
