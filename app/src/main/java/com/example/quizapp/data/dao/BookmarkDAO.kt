package com.example.quizapp.data.dao

import kotlinx.coroutines.flow.Flow
import androidx.room.*
import com.example.quizapp.data.model.Bookmark

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)

    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("SELECT * FROM bookmarks")
    fun getAllBookmarks(): Flow<List<Bookmark>>

    @Query("SELECT * FROM bookmarks WHERE question = :question")
    fun getBookmarkByQuestion(question: String): Flow<Bookmark?>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE questionUUID = :questionUUID)")
    fun isQuestionBookmarked(questionUUID: String): Flow<Boolean>
}
