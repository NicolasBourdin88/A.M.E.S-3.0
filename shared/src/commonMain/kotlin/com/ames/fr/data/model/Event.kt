package com.ames.fr.data.model

import kotlinx.serialization.Contextual
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
    val x: Float? = null,
    val y: Float? = null,
    val originWidth: Float? = null,
    val originHeight: Float? = null,
    val anchor: String? = null,
    val isTappable: Boolean? = null,
    val eventName: String? = null,
    val duration: Int? = null,
    val fontSize: Float? = null,
    val printSpeed: Float? = null,
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
