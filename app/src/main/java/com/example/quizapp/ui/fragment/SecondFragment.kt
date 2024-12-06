package com.example.quizapp.ui.fragment

import android.os.Bundle
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
import com.example.quizapp.data.model.QuestionListItem
import com.example.quizapp.databinding.FragmentSecondBinding
import com.example.quizapp.ui.adapter.QuestionRecyclerViewAdapter
import com.example.quizapp.ui.viewModel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val mViewModel: QuizViewModel by viewModels()
    private lateinit var mQuestionAdapter: QuestionRecyclerViewAdapter
    private lateinit var mQuestionList: ArrayList<QuestionListItem>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectData()
        mViewModel.getQuestionList()

        mQuestionAdapter = QuestionRecyclerViewAdapter(arrayListOf())
        val layoutMan = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        _binding?.questionRV?.apply {
            adapter = mQuestionAdapter
            layoutManager = layoutMan
        }
        // Attach PageSnapHelper for swipeable paging behavior
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(_binding?.questionRV)

        _binding?.nextBtn?.setOnClickListener {
            val currentPosition = layoutMan.findFirstVisibleItemPosition()
            if (currentPosition < mQuestionList.size - 1) {
                _binding?.questionRV?.smoothScrollToPosition(currentPosition + 1)
            } else {
                //TODO :- Submit quiz or navigate to results
                Toast.makeText(context, "Quiz Submitted!", Toast.LENGTH_SHORT).show()
                calculateResult()
            }
        }

        _binding?.questionRV?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val currentPosition = layoutMan.findFirstVisibleItemPosition()

                    // Enable the Next button if not on the last question
//                    _binding?.nextBtn?.isEnabled = currentPosition < mQuestionList.size - 1
                    // Disable Next button on the last question
                    if (currentPosition == mQuestionList.size - 1) {
                        _binding?.nextBtn?.text = "Submit"
                    } else {
                        _binding?.nextBtn?.text = "Next"
                    }
                }
            }
        })
    }

    private fun calculateResult() {
        if (this::mQuestionList.isInitialized) {
            val totalNoOfQuestions = mQuestionList.size
            var totalCorrectAns = 0
            mQuestionList.forEach {
                if(it.correctOption == it.selectedOption){
                    totalCorrectAns++
                }
            }
            _binding?.quizCl?.visibility = View.GONE
            _binding?.ansCl?.visibility = View.VISIBLE

            _binding?.ansTv?.text = "${totalCorrectAns} / ${totalNoOfQuestions} \n Correct"

        }


    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.questionList.collect {
                    when (it) {
                        is ApiResult.Error -> {

                        }

                        is ApiResult.Loading -> {

                        }

                        is ApiResult.Success -> {
                            mQuestionList = it.data ?: arrayListOf()
                            mQuestionAdapter.updateData(it.data ?: arrayListOf())
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}