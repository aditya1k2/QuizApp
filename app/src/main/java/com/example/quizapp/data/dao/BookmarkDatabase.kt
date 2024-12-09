package com.example.quizapp.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizapp.data.model.Bookmark

@Database(entities = [Bookmark::class], version = 1, exportSchema = false)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}
