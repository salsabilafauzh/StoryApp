package com.example.storyapp.data.remote.config

import com.example.storyapp.data.remote.model.UserModel
import com.example.storyapp.data.remote.response.ResponseGeneral
import com.example.storyapp.data.remote.response.result.ResponseLogin
import com.example.storyapp.data.remote.response.result.ResponseGetAllStories
import com.example.storyapp.data.remote.response.result.ResponseGetDetailStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    fun register(
        @Body user: UserModel
    ): Call<ResponseGeneral>

    @POST("login")
    fun login(
        @Body user: UserModel
    ): Call<ResponseLogin>

    @POST("stories")
    @Multipart
    fun createStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<ResponseGeneral>

    @POST("stories")
    @Multipart
    fun createStoryEnableLocation(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double,
    ): Call<ResponseGeneral>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ResponseGetAllStories>

    @GET("stories")
    fun getAllStoriesLocation(
        @Query("location") location: Int = 1,
        @Query("size") size: Int = 50
    ): Call<ResponseGetAllStories>

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id: String
    ): Call<ResponseGetDetailStory>
}