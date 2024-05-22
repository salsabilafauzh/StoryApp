package com.example.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.AuthRepository
import com.example.storyapp.data.remote.response.ResponseGeneral

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun registerUser(
        name: String,
        email: String,
        password: String,
    ):LiveData<ResponseGeneral> {
        return authRepository.registerUser(name, email, password)
    }


    fun isLoading(): LiveData<Boolean> {
        return authRepository.isLoading()
    }
}