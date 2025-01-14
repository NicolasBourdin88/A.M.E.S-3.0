package com.ames.fr.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Event(
    @Contextual val uuid: Uuid = Uuid.random(),
    val type: TypeEvent,
    val fileName: String? = null,
    val text: String? = null,
    val nbImage: Int? = null,
    val x: Float? = null,
    val y: Float? = null,
    val xBegin: Float? = null,
    val yBegin: Float? = null,
    val xEnd: Float? = null,
    val yEnd: Float? = null,
    val originWidth: Float? = null,
    val originHeight: Float? = null,
    val anchor: String? = null,
    val isTappable: Boolean? = null,
    val eventName: String? = null,
    val duration: Int? = null,
    val fontSize: Float? = null,
    val printSpeed: Float? = null,
    @SerialName("loop")
    val isLoop: Boolean? = null,
    val frameRate: Float? = null,
    val imageWidth: Float? = null,
    val imageHeight: Float? = null,
    val modifiedWidth: Float? = null,
    val modifiedHeight: Float? = null,
    val animationSpeed: Float? = null,
    val contentScale: ContentScale? = null,
    @SerialName("activate")
    val isTorchLightActivated: Boolean? = null,
)

@Serializable
enum class ContentScale {
    @SerialName("fullScreen")
    FULL_SCREEN,

    @SerialName("imageSize")
    IMAGE_SIZE
}

@Serializable
enum class TypeEvent {
    SP,//clickable image
    TC,//text
    SR,//stop read
    RMA,//remove all
    SO,//soung
    AI,//animation
    WA,//wait
    GM,//gamemode
    RM,//remove
    AT,//animated text
    MI,//move image
    CA,//camera
    DT,//date time
    TL,//torch light
}

