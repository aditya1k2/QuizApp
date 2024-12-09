package com.example.quizapp.data.repoImpl

import com.example.quizapp.data.ApiResult
import com.example.quizapp.data.NetworkUtils
import com.example.quizapp.data.api.ApiService
import com.example.quizapp.data.dao.BookmarkDao
import com.example.quizapp.data.dao.BookmarkDatabase
import com.example.quizapp.data.model.Bookmark
import com.example.quizapp.data.model.QuestionList
import com.example.quizapp.data.model.QuestionListItem
import com.example.quizapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val bookmarkDatabase: BookmarkDao
) : Repository {

    override suspend fun getQuestions(): Flow<ApiResult<ArrayList<QuestionListItem>>> {
        return NetworkUtils.toResultFlow {
            apiService.getQuestions()
        }
    }

    override suspend fun insertBookmark(bookmark: Bookmark) {
        bookmarkDatabase.insertBookmark(bookmark = bookmark)
    }

    override suspend fun getAllBookmarks(): Flow<List<Bookmark>> {
        return bookmarkDatabase.getAllBookmarks()
    }

    override suspend fun isQuestionBookmarked(questionUUID: String): Flow<Boolean> {
        return bookmarkDatabase.isQuestionBookmarked(questionUUID = questionUUID )
    }

}