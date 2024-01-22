package com.example.inhouseapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<Pair<Boolean, String>>()
    val loginResult: LiveData<Pair<Boolean, String>> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(username: String, password: String) {
        _isLoading.value = true

        // execute Retrofit login request
        val loginApi = RetrofitClient.getInstance().create(ApiInterface::class.java)
        val call = loginApi.login(5, LoginRequest(username, password))
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    // login success
                    _loginResult.value = Pair(true, response.body()?.token!!)
                } else {
                    // login failure
                    _loginResult.value = Pair(false, Gson().fromJson(response.errorBody()?.string(), Error::class.java).error)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: " + t.message)
                _isLoading.value = false
                _loginResult.value = Pair(false, "Network error. Please try again.")
            }
        })
    }

    companion object {
        const val TAG = "LoginViewModel"
    }
}
