package com.example.storyapp.ui.submitStory


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.data.remote.response.ResponseGeneral
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class SubmitStoryViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    fun postStory(photo: File, description: String): LiveData<ResponseGeneral> {
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", photo.name, requestImageFile)
        return storiesRepository.createStory(imageMultipart, descriptionPart)
    }

    fun postWithLocation(
        photo: File,
        description: String,
        lat: Double,
        lon: Double
    ): LiveData<ResponseGeneral> {
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", photo.name, requestImageFile)
        return storiesRepository.createStoryWithLocation(imageMultipart, descriptionPart, lat, lon)
    }

    fun isLoading(): LiveData<Boolean> {
        return storiesRepository.isLoading()
    }
}