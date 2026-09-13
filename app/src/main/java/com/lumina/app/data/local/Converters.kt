package com.lumina.app.data.local

import androidx.room.TypeConverter
import com.lumina.app.data.model.EventKind
import com.lumina.app.data.model.Mood
import com.lumina.app.data.model.Priority

class LuminaConverters {

    @TypeConverter
    fun stringListToString(value: List<String>): String = value.joinToString("|")

    @TypeConverter
    fun stringToStringList(value: String): List<String> =
        if (value.isBlank()) emptyList() else value.split("|")

    @TypeConverter fun moodToString(value: Mood): String = value.name
    @TypeConverter fun stringToMood(value: String): Mood = Mood.valueOf(value)

    @TypeConverter fun priorityToString(value: Priority): String = value.name
    @TypeConverter fun stringToPriority(value: String): Priority = Priority.valueOf(value)

    @TypeConverter fun kindToString(value: EventKind): String = value.name
    @TypeConverter fun stringToKind(value: String): EventKind = EventKind.valueOf(value)
}
