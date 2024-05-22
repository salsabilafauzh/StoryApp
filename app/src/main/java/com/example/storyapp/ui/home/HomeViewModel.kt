package com.example.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.data.local.database.story.StoryEntity

class HomeViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    fun loadStories(): LiveData<PagingData<StoryEntity>> {
        return storiesRepository.getAllStories().cachedIn(viewModelScope)
    }
}