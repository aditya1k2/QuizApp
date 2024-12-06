package com.example.quizapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.ApiResult
import com.example.quizapp.data.model.QuestionListItem
import com.example.quizapp.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _questionList: MutableStateFlow<ApiResult<ArrayList<QuestionListItem>>> = MutableStateFlow(ApiResult.Loading)
    val questionList = _questionList.asStateFlow()

    fun getQuestionList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuestions().collect {
                _questionList.value = it
            }
        }
    }

}