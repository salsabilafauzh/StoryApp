package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.AuthRepository
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.data.local.UserPreferences
import com.example.storyapp.data.local.dataStore
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.remote.config.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideStoryRepository(context: Context): StoriesRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken().first() }
        val apiService = ApiConfig.getApiInstance(user)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoriesRepository.getInstance(storyDatabase, apiService)
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiInstance("")
        return AuthRepository.getInstance(apiService, pref)
    }
}