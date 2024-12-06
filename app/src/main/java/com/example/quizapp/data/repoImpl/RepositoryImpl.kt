package com.example.quizapp.data.repoImpl

import com.example.quizapp.data.ApiResult
import com.example.quizapp.data.NetworkUtils
import com.example.quizapp.data.api.ApiService
import com.example.quizapp.data.model.QuestionList
import com.example.quizapp.data.model.QuestionListItem
import com.example.quizapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: ApiService) : Repository {

    override suspend fun getQuestions(): Flow<ApiResult<ArrayList<QuestionListItem>>> {
        return NetworkUtils.toResultFlow {
            apiService.getQuestions()
        }
    }

}