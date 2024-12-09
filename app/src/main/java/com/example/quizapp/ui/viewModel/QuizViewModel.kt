package com.example.quizapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.ApiResult
import com.example.quizapp.data.model.Bookmark
import com.example.quizapp.data.model.QuestionListItem
import com.example.quizapp.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _questionList: MutableStateFlow<ApiResult<ArrayList<QuestionListItem>>> =
        MutableStateFlow(ApiResult.Loading)
    val questionList = _questionList.asStateFlow()

    fun getQuestionList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuestions().collect {
                _questionList.value = it
            }
        }
    }

    fun insertBookmark(bookmark: Bookmark) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBookmark(bookmark = bookmark)
        }
    }

    private val _allBookMark: MutableStateFlow<List<Bookmark>> = MutableStateFlow(arrayListOf())
    val allBookmark = _allBookMark.asStateFlow()
    fun getAllBookmark() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllBookmarks().collect {
                _allBookMark.value = it
            }
        }
    }

    private val _isQuestionBookMarked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isQuestionBookmarked = _isQuestionBookMarked.asStateFlow()

    fun isQuestionBookmarked(questionUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.isQuestionBookmarked(questionUUID = questionUUID).collect {
                _isQuestionBookMarked.value = it
            }
        }
    }

}