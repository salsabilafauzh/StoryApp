package com.example.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseGeneral(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("isLoading")
    val isLoading: Boolean = false
)
