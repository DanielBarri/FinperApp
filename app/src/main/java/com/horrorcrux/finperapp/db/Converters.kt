package com.horrorcrux.finperapp.db

import androidx.room.TypeConverter
import com.horrorcrux.finperapp.db.models.TransactionType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.value
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.fromString(value)
    }
}