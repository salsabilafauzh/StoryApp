package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.local.UserPreferences
import com.example.storyapp.data.remote.config.ApiService
import com.example.storyapp.data.remote.model.UserModel
import com.example.storyapp.data.remote.response.ResponseGeneral
import com.example.storyapp.data.remote.response.result.ResponseLogin
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
    private var _isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun loginUser(email: String, password: String): LiveData<ResponseLogin> {
        _isLoading.value = true
        val resultLiveData = MutableLiveData<ResponseLogin>()
        val user = UserModel(email = email, password = password)
        val client = apiService.login(user)
        client.enqueue(
            object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            resultLiveData.value = response.body()
                            val resBody = response.body()
                            if (resBody?.loginResult != null) {
                                runBlocking {
                                    resBody.loginResult.token?.let {
                                        userPreferences.saveTokenUser(
                                            it
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        resultLiveData.value =
                            ResponseLogin(error = true, message = response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    _isLoading.value = false
                    resultLiveData.value =
                        ResponseLogin(error = true, message = "500")
                }
            }
        )
        return resultLiveData
    }


    fun registerUser(
        name: String,
        email: String,
        password: String
    ): LiveData<ResponseGeneral> {
        _isLoading.value = true
        val resultLiveData = MutableLiveData<ResponseGeneral>()
        val user = UserModel(name = name, email = email, password = password)
        val client = apiService.register(user)
        client.enqueue(
            object : Callback<ResponseGeneral> {
                override fun onResponse(
                    call: Call<ResponseGeneral>,
                    response: Response<ResponseGeneral>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                    } else {
                        resultLiveData.value =
                            ResponseGeneral(error = true, message = response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ResponseGeneral>, t: Throwable) {
                    _isLoading.value = false
                    resultLiveData.value =
                        ResponseGeneral(error = true, message = t.message.toString())
                }
            }
        )
        return resultLiveData
    }

    fun isLoading(): LiveData<Boolean> {
        return _isLoading
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userPreferences)
            }.also { instance = it }
    }
}