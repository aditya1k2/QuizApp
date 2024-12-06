package com.example.quizapp.domain.repository

import com.example.quizapp.data.ApiResult
import com.example.quizapp.data.model.QuestionList
import com.example.quizapp.data.model.QuestionListItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {
    suspend fun getQuestions(): Flow<ApiResult<ArrayList<QuestionListItem>>>
}