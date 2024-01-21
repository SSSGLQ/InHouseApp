package com.example.inhouseapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Query("delay") query: Int, @Body request: LoginRequest): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @GET("users")
    fun getStaffList(@Query("page") query: Int): Call<StaffPageData>
}