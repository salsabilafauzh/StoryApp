package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.AuthRepository
import com.example.storyapp.data.remote.response.result.ResponseLogin


class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun loginUser(email: String, password: String): LiveData<ResponseLogin> {
        return authRepository.loginUser(email, password)
    }

    fun isLoading(): LiveData<Boolean> {
        return authRepository.isLoading()
    }
}