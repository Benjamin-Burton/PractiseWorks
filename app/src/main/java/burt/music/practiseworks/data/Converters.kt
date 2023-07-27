package burt.music.practiseworks.data

import androidx.room.TypeConverter
import java.util.*

/**
 * Type converters for the database - converting between Java Date
 *  objects and Longs (millis, i think)
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}