package com.example.quizapp.data.api

import com.example.quizapp.data.model.QuestionList
import com.example.quizapp.data.model.QuestionListItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET("v3/c87ccdd0-7d77-481f-80c8-21bcb67241a3")
    suspend fun getQuestions(): Response<ArrayList<QuestionListItem>>
}