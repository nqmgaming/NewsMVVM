package com.nqmgaming.newsmvvm.db

import androidx.room.TypeConverter
import com.nqmgaming.newsmvvm.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}