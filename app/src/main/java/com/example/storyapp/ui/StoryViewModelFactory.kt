package com.example.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.di.Injection
import com.example.storyapp.ui.home.HomeViewModel
import com.example.storyapp.ui.detailStory.DetailStoryViewModel
import com.example.storyapp.ui.maps.MapsViewModel
import com.example.storyapp.ui.submitStory.SubmitStoryViewModel


class StoryViewModelFactory(
    private val storiesRepository: StoriesRepository
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            HomeViewModel::class.java -> return HomeViewModel(storiesRepository) as T
            DetailStoryViewModel::class.java -> return DetailStoryViewModel(storiesRepository) as T
            SubmitStoryViewModel::class.java -> return SubmitStoryViewModel(storiesRepository) as T
            MapsViewModel::class.java -> return MapsViewModel(storiesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): StoryViewModelFactory {
            INSTANCE = StoryViewModelFactory(Injection.provideStoryRepository(context))
            return INSTANCE as StoryViewModelFactory
        }
    }
}