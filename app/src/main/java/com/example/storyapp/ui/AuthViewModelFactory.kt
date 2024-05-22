package com.example.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.AuthRepository
import com.example.storyapp.di.Injection
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.register.RegisterViewModel

class AuthViewModelFactory(
    private val authRepository: AuthRepository
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            LoginViewModel::class.java -> return LoginViewModel(authRepository) as T
            RegisterViewModel::class.java -> return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): AuthViewModelFactory {
            instance ?: synchronized(AuthViewModelFactory::class.java) {
                instance = AuthViewModelFactory(Injection.provideAuthRepository(context))
            }
            return instance as AuthViewModelFactory
        }
    }
}