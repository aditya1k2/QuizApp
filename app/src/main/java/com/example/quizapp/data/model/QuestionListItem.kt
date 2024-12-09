package com.example.quizapp.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class QuestionListItem(
    @SerializedName("correctOption")
    val correctOption: Int? = null,
    @SerializedName("option1")
    val option1: String? = null,
    @SerializedName("option2")
    val option2: String? = null,
    @SerializedName("option3")
    val option3: String? = null,
    @SerializedName("option4")
    val option4: String? = null,
    @SerializedName("question")
    val question: String? = null,
    @SerializedName("questionType")
    val questionType: String? = null,
    @SerializedName("solution")
    val solution: List<Solution?>? = null,
    @SerializedName("sort")
    val sort: Int? = null,
    @SerializedName("uuidIdentifier")
    val uuidIdentifier: String? = null,
    var selectedOption: Int? = null,
    var checkedId: Int? = null
) : Serializable