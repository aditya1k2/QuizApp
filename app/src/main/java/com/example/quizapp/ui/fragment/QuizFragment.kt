package com.example.quizapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.data.ApiResult
import com.example.quizapp.data.model.Bookmark
import com.example.quizapp.data.model.QuestionListItem
import com.example.quizapp.databinding.FragmentQuizBinding
import com.example.quizapp.ui.adapter.QuestionRecyclerViewAdapter
import com.example.quizapp.ui.viewModel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class QuizFragment : Fragment() {

    private lateinit var mBinding: FragmentQuizBinding
    private val mViewModel: QuizViewModel by viewModels()
    private lateinit var mQuestionAdapter: QuestionRecyclerViewAdapter
    private lateinit var mQuestionList: ArrayList<QuestionListItem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentQuizBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectData()
        mViewModel.getQuestionList()

        mQuestionAdapter = QuestionRecyclerViewAdapter(selectedOption = { position, selectedOption ->
                mQuestionList[position].selectedOption = selectedOption
        })
        val layoutMan = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mBinding.questionRV.apply {
            adapter = mQuestionAdapter
            layoutManager = layoutMan
        }
        // Attach PageSnapHelper for swipeable paging behavior
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(mBinding.questionRV)

        mBinding.nextBtn.setOnClickListener {
            val currentPosition = layoutMan.findFirstVisibleItemPosition()
            if (currentPosition < mQuestionList.size - 1) {
                mBinding.questionRV.smoothScrollToPosition(currentPosition + 1)
            } else {
                Toast.makeText(context, "Quiz Submitted!", Toast.LENGTH_SHORT).show()
                calculateResult()
            }
        }

        mBinding.bookmarkBtn.setOnClickListener {
            val currentPosition = layoutMan.findFirstVisibleItemPosition()
            val currentQuestion = mQuestionList[currentPosition]
            mViewModel.insertBookmark(
                Bookmark(
                    question = currentQuestion.question ?: "",
                    option1 = currentQuestion.option1 ?: "",
                    option2 = currentQuestion.option2 ?: "",
                    option3 = currentQuestion.option3 ?: "",
                    option4 = currentQuestion.option4 ?: "",
                    questionUUID = currentQuestion.uuidIdentifier ?: "",
                    selectedOption = currentQuestion.selectedOption,
                )
            )
            currentQuestion.uuidIdentifier?.let { it1 -> mViewModel.isQuestionBookmarked(it1) }
        }

        mBinding.questionRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val currentPosition = layoutMan.findFirstVisibleItemPosition()
                    mQuestionList[currentPosition].uuidIdentifier?.let {
                        mViewModel.isQuestionBookmarked(
                            it
                        )
                    }
                    if (currentPosition == mQuestionList.size - 1) {
                        mBinding.nextBtn.text = getString(R.string.submit)
                    } else {
                        mBinding.nextBtn.text = getString(R.string.next)
                    }
                }
            }
        })

        mBinding.continuBtn.setOnClickListener {
            findNavController().navigate(R.id.action_QuizFragment_to_HomeFragment)
        }
    }

    private fun calculateResult() {
        if (this::mQuestionList.isInitialized) {
            val totalNoOfQuestions = mQuestionList.size
            var totalCorrectAns = 0
            mQuestionList.forEach {
                if (it.correctOption == it.selectedOption) {
                    totalCorrectAns++
                }
            }
            mBinding.quizCl.visibility = View.GONE
            mBinding.ansCl.visibility = View.VISIBLE
            mBinding.ansTv.text = "${totalCorrectAns} / ${totalNoOfQuestions} \nCorrect"
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.questionList.collect {
                    when (it) {
                        is ApiResult.Error -> {
                            mBinding.progressBar.visibility = View.GONE
                            mBinding.quizCl.visibility = View.GONE
                        }

                        is ApiResult.Loading -> {
                            mBinding.progressBar.visibility = View.VISIBLE
                            mBinding.quizCl.visibility = View.GONE

                        }

                        is ApiResult.Success -> {
                            mBinding.progressBar.visibility = View.GONE
                            mBinding.quizCl.visibility = View.VISIBLE

                            mQuestionList = it.data ?: arrayListOf()
                            mQuestionAdapter.updateData(mQuestionList)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.isQuestionBookmarked.collect {
                    if (it) {
                        mBinding.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_filled)
                    } else {
                        mBinding.bookmarkBtn.setImageResource(R.drawable.ic_bookmark)

                    }
                }
            }
        }
    }
}