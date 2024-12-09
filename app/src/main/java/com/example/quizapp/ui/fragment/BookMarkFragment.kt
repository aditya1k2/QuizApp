package com.example.quizapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.quizapp.R
import com.example.quizapp.data.model.Bookmark
import com.example.quizapp.data.model.QuestionListItem
import com.example.quizapp.databinding.FragmentBookMarkBinding
import com.example.quizapp.databinding.FragmentQuizBinding
import com.example.quizapp.ui.adapter.BookMarkAdapter
import com.example.quizapp.ui.adapter.QuestionRecyclerViewAdapter
import com.example.quizapp.ui.viewModel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookMarkFragment : Fragment() {

    private lateinit var mBinding: FragmentBookMarkBinding
    private val mViewModel: QuizViewModel by viewModels()
    private lateinit var mBookmarkAdapter: BookMarkAdapter
    private lateinit var mQuestionList: List<Bookmark>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentBookMarkBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getAllBookmark()
        collectData()

        mBookmarkAdapter = BookMarkAdapter(selectedOption = { position, selectedOption ->

        })
        val layoutMan = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mBinding.questionRV.apply {
            adapter = mBookmarkAdapter
            layoutManager = layoutMan
        }
        // Attach PageSnapHelper for swipeable paging behavior
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(mBinding.questionRV)
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.allBookmark.collect {
                    mQuestionList = it
                    mBookmarkAdapter.updateData(mQuestionList)
                }
            }
        }
    }
}