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
    var duration: Float? = null,
    val eventName: String? = null,

    val button: Button? = null,
    val text: Text? = null,
    val dateTime: DateTime? = null,
    val ghost: Ghost? = null,
    val torchLight: TorchLight? = null,
    val sound: Sound? = null,
    val movableImage: MovableImage? = null,
    val animation: Animation? = null,
) {
    companion object {
        @Serializable
        enum class TypeEvent {
            SP,     // clickableImage
            TC,     // text
            SR,     // stopRead
            RMA,    // removeAll
            SO,     // sound
            AI,     // animation
            WA,     // wait
            GM,     // gameMode
            RM,     // remove
            AT,     // animatedText
            MI,     // move image
            CA,     // camera
            DT,     // dateTime
            TL,     // torchLight
            SC,     // scan
            GH,     // ghost
        }
    }
}
