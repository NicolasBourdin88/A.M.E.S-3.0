package com.ames.fr.data

import com.ames.fr.data.model.Event
import kotlinx.serialization.json.Json

class ScriptManager {
    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }

        fun getEventList(jsonValue: String): List<Event> {
            return json.decodeFromString<List<Event>>(jsonValue)
        }
    }
}
