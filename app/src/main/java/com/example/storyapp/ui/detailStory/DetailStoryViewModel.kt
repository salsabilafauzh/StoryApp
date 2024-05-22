package com.example.storyapp.ui.detailStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.data.remote.response.result.ResponseGetDetailStory

class DetailStoryViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    fun getDetail(id: String): LiveData<ResponseGetDetailStory> {
       return storiesRepository.getDetailStory(id)
    }

    fun isLoading(): LiveData<Boolean> {
        return storiesRepository.isLoading()
    }
}