package com.example.inhouseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StaffListActivity : AppCompatActivity() {

    lateinit var staffRecyclerView: RecyclerView
    lateinit var staffAdapter: StaffAdapter
    private lateinit var retrofitBuilder: ApiInterface
    private var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_list)

        staffRecyclerView = findViewById(R.id.recyclerView)
        retrofitBuilder = RetrofitClient.getInstance().create(ApiInterface::class.java)

        getStaffData()

        // get login token
        val token = intent.getStringExtra("TOKEN")
        val loginToken = findViewById<TextView>(R.id.loginToken)

        // do something with token
        if (token != null) {
            loginToken.text = token
            loginToken.visibility = View.VISIBLE
        } else {
            // process the case that token is null
            Toast.makeText(this, "Token is null", Toast.LENGTH_SHORT).show()
        }
        val loadMore = findViewById<Button>(R.id.loadMoreBtn)
        loadMore.setOnClickListener {
            ++page
            getStaffData()
        }
        val lastPage = findViewById<Button>(R.id.lastPageBtn)
        lastPage.setOnClickListener {
            --page
            getStaffData()
        }

    }

    private fun getStaffData() {
        val staffData = retrofitBuilder.getStaffList(page)
        staffData.enqueue(object : Callback<StaffPageData?>{
            override fun onResponse(call: Call<StaffPageData?>, response: Response<StaffPageData?>) {
                if (response.isSuccessful) {
                    // if the API call is a success then this method is executed
                    val dataList = response.body()?.data!!
                    staffAdapter = StaffAdapter(this@StaffListActivity, dataList)
                    staffRecyclerView.adapter = staffAdapter
                    staffRecyclerView.layoutManager = LinearLayoutManager(this@StaffListActivity)
                    Log.d(TAG, "onResponse: " + response.body())
                    val page = response.body()?.page
                    val totalPages = response.body()?.total_pages
                    val loadMore = findViewById<Button>(R.id.loadMoreBtn)
                    val lastPage = findViewById<Button>(R.id.lastPageBtn)
                    if (page != totalPages) {
                        loadMore.visibility = View.VISIBLE
                    } else {
                        loadMore.visibility = View.GONE
                    }
                    if (page != 1) {
                        lastPage.visibility = View.VISIBLE
                    } else {
                        lastPage.visibility = View.GONE
                    }
                } else {
                    Log.d(TAG, "onFail: " + response.body())
                }
            }

            override fun onFailure(call: Call<StaffPageData?>, t: Throwable) {
                // if the API call is a failure then this method is executed
                Log.d(TAG, "onFailure: " + t.message)
            }
        })
    }

    companion object {
        const val TAG = "StaffListActivity"
    }
}