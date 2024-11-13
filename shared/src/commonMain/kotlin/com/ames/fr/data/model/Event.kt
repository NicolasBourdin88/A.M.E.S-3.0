package com.ames.fr.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val type: TypeEvent,
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
