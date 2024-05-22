package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.local.database.story.StoryEntity
import com.example.storyapp.data.remote.config.ApiService
import com.example.storyapp.data.remote.response.ResponseGeneral
import com.example.storyapp.data.remote.response.result.ResponseGetAllStories
import com.example.storyapp.data.remote.response.result.ResponseGetDetailStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesRepository constructor(
    private val database: StoryDatabase,
    private val apiService: ApiService,
) {
    private var _isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun createStory(
        photo: MultipartBody.Part,
        description: RequestBody
    ): LiveData<ResponseGeneral> {
        val resultLiveData = MutableLiveData<ResponseGeneral>()
        _isLoading.value = true
        val client = apiService.createStory(photo, description)
        client.enqueue(
            object : Callback<ResponseGeneral> {
                override fun onResponse(
                    call: Call<ResponseGeneral>,
                    response: Response<ResponseGeneral>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ResponseGeneral>, t: Throwable) {
                Log.d(TAG,t.message.toString())
                }
            }
        )
        return resultLiveData
    }

    fun createStoryWithLocation(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Double,
        lon: Double
    ): LiveData<ResponseGeneral> {
        val resultLiveData = MutableLiveData<ResponseGeneral>()
        _isLoading.value = true
        val client = apiService.createStoryEnableLocation(photo, description, lat, lon)
        client.enqueue(
            object : Callback<ResponseGeneral> {
                override fun onResponse(
                    call: Call<ResponseGeneral>,
                    response: Response<ResponseGeneral>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ResponseGeneral>, t: Throwable) {
                    Log.d(TAG,t.message.toString())
                }
            }
        )
        return resultLiveData
    }

    fun getAllStories(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStories()
            }
        ).liveData
    }


    fun getAllWithLocation(): LiveData<ResponseGetAllStories> {
        val resultLiveData = MutableLiveData<ResponseGetAllStories>()
        _isLoading.value = true
        val client = apiService.getAllStoriesLocation()
        client.enqueue(
            object : Callback<ResponseGetAllStories> {
                override fun onResponse(
                    call: Call<ResponseGetAllStories>,
                    response: Response<ResponseGetAllStories>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ResponseGetAllStories>, t: Throwable) {
                    Log.d(TAG,t.message.toString())
                }
            }
        )
        return resultLiveData
    }


    fun getDetailStory(idStory: String): LiveData<ResponseGetDetailStory> {
        val resultLiveData = MutableLiveData<ResponseGetDetailStory>()
        _isLoading.value = true
        val client = apiService.getDetailStory(idStory)
        client.enqueue(
            object : Callback<ResponseGetDetailStory> {
                override fun onResponse(
                    call: Call<ResponseGetDetailStory>,
                    response: Response<ResponseGetDetailStory>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        resultLiveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ResponseGetDetailStory>, t: Throwable) {
                    Log.d(TAG,t.message.toString())
                }
            }
        )
        return resultLiveData
    }

    fun isLoading(): LiveData<Boolean> {
        return _isLoading
    }

    companion object {
        const val TAG = "StoriesRepository"
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ) = StoriesRepository(storyDatabase, apiService)
    }
}