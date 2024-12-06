package com.example.quizapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.data.model.QuestionListItem
import com.example.quizapp.databinding.ItemQuestionBinding

class QuestionRecyclerViewAdapter(
    private val questions: ArrayList<QuestionListItem>
) : RecyclerView.Adapter<QuestionRecyclerViewAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: QuestionListItem) {
            binding.questionText.text = question.question
            binding.optionsGroup.clearCheck()
            // Dynamically add options to the RadioGroup
            binding.option1Rb.text = question.option1
            binding.option2Rb.text = question.option2
            binding.option3Rb.text = question.option3
            binding.option4Rb.text = question.option4

            // Clear any previous listeners to avoid multiple triggers
            binding.optionsGroup.setOnCheckedChangeListener(null)
            // Restore selected state
            question.selectedOption?.let { selectedIndex ->
                binding.optionsGroup.check(selectedIndex)
            } ?: binding.optionsGroup.clearCheck()

            // Save selected state when user interacts
            binding.optionsGroup.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId != -1) {
                    question.selectedOption = checkedId
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question = question)

    }

    override fun getItemCount(): Int = questions.size

    fun updateData(questions: ArrayList<QuestionListItem>) {
        this.questions.clear()
        this.questions.addAll(questions)
        notifyDataSetChanged()
    }
}

