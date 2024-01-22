package com.example.inhouseapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StaffViewModel : ViewModel() {
    private val _staffListResult = MutableLiveData<StaffPageData>()
    val staffListResult: LiveData<StaffPageData> = _staffListResult

    fun getStaffDate(page: Int) {
        // execute Retrofit get staff list data request
        val getStaffDataApi = RetrofitClient.getInstance().create(ApiInterface::class.java)
        val call = getStaffDataApi.getStaffList(page)
        call.enqueue(object : Callback<StaffPageData> {
            override fun onResponse(call: Call<StaffPageData>, response: Response<StaffPageData>) {
                if (response.isSuccessful) {
                    // get staff data success
                    _staffListResult.value = response.body()
                } else {
                    // get staff data failure
                    Log.d(TAG, "onFail: " + response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<StaffPageData>, t: Throwable) {
                Log.d(TAG, "onFailure: " + t.message)
            }
        })
    }

    companion object {
        const val TAG = "StaffViewModel"
    }
}