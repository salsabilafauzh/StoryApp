package com.example.storyapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserModel(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
