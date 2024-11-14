package com.ames.fr.data

import com.ames.fr.data.model.RawEvent
import kotlinx.serialization.json.Json

class ScriptManager {
    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }

        fun getEventList(jsonValue: String): List<RawEvent> {
            return json.decodeFromString<List<RawEvent>>(jsonValue)
        }
    }
}
