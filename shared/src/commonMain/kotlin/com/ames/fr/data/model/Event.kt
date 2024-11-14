package com.ames.fr.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val type: TypeEvent,
    val fileName: String? = null,
    val text: String? = null,
    val x: Float? = null,
    val y: Float? = null,
    val originWidth: Float? = null,
    val originHeight: Float? = null,
    val anchor: String? = null,
    val isTappable: Boolean? = null,
    val eventName: String? = null,
    val duration: Int? = null,
)

@Serializable
enum class TypeEvent {
    SP,
    TC,
    SR,
    RMA,
    SO,
    AI,
    WA,
    GM,
    RM,
    AT,
    MI,
    STS,
    CA,
    DT,
    TL,
}
