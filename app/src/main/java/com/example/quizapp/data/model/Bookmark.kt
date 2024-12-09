package com.example.quizapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val questionUUID: String,
    val selectedOption: Int? = null // User's selected answer
)
