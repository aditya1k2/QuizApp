package com.example.quizapp.ui.adapter

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.data.model.QuestionListItem
import com.example.quizapp.databinding.ItemQuestionBinding

class QuestionRecyclerViewAdapter(
    private val questions: ArrayList<QuestionListItem> = arrayListOf(),
    val selectedOption: (position: Int, selectedOption: Int) -> Unit
) : RecyclerView.Adapter<QuestionRecyclerViewAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: QuestionListItem) {
            binding.questionText.text = question.question
            binding.questionText.movementMethod = ScrollingMovementMethod()
            binding.optionsGroup.clearCheck()
            // Dynamically add options to the RadioGroup
            binding.option1Rb.text = question.option1
            binding.option2Rb.text = question.option2
            binding.option3Rb.text = question.option3
            binding.option4Rb.text = question.option4


            // Restore selected state
            question.checkedId?.let { checkedId ->
                binding.optionsGroup.check(checkedId)
            } ?: binding.optionsGroup.clearCheck()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        // Clear any previous listeners to avoid multiple triggers
        holder.binding.optionsGroup.setOnCheckedChangeListener(null)
        // Save selected state when user interacts
        holder.binding.optionsGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val selectedPosition = radioGroup.indexOfChild(holder.binding.root.findViewById(checkedId))
            if (checkedId != -1) {
                questions[position].checkedId = checkedId
                selectedOption(position, selectedPosition + 1)
            }
        }
        holder.bind(question = question)
    }

    override fun getItemCount(): Int = questions.size

    fun updateData(questions: ArrayList<QuestionListItem>) {
        this.questions.clear()
        this.questions.addAll(questions)
        notifyDataSetChanged()
    }
}

