package com.example.quizapp.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Solution(
    @SerializedName("contentData")
    val contentData: String? = null,
    @SerializedName("contentType")
    val contentType: String? = null
) : Serializable