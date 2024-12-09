package com.example.quizapp.data.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromList(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun toList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }
}
