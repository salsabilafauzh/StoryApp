package com.example.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.data.remote.response.result.ResponseGetAllStories

class MapsViewModel(
    private val storiesRepository: StoriesRepository
) : ViewModel() {

    private var _listStories: MutableLiveData<ResponseGetAllStories> = MutableLiveData()
    val listStories: LiveData<ResponseGetAllStories> = _listStories

    fun getLocationStory(): LiveData<ResponseGetAllStories> {
        return storiesRepository.getAllWithLocation()
    }

    fun showLoading(): LiveData<Boolean> {
        return storiesRepository.isLoading()
    }
}