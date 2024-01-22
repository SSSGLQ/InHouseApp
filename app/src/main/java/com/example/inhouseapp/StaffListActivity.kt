package com.example.inhouseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StaffListActivity : AppCompatActivity() {

    private lateinit var staffRecyclerView: RecyclerView
    private lateinit var staffAdapter: StaffAdapter
    private lateinit var retrofitBuilder: ApiInterface
    private lateinit var staffViewModel: StaffViewModel
    private var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_list)

        staffViewModel = ViewModelProvider(this)[StaffViewModel::class.java]

        staffRecyclerView = findViewById(R.id.recyclerView)
        retrofitBuilder = RetrofitClient.getInstance().create(ApiInterface::class.java)

        val loadMore = findViewById<Button>(R.id.loadMoreBtn)
        val prevPage = findViewById<Button>(R.id.prevPageBtn)
        staffViewModel.staffListResult.observe(this) { result ->
            val dataList = result?.data!!
            staffAdapter = StaffAdapter(this@StaffListActivity, dataList)
            staffRecyclerView.adapter = staffAdapter
            staffRecyclerView.layoutManager = LinearLayoutManager(this@StaffListActivity)
            val page = result.page
            val totalPages = result.total_pages

            if (page != totalPages) {
                loadMore.visibility = View.VISIBLE
            } else {
                loadMore.visibility = View.GONE
            }
            if (page != 1) {
                prevPage.visibility = View.VISIBLE
            } else {
                prevPage.visibility = View.GONE
            }
        }

        staffViewModel.getStaffDate(page)

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
        loadMore.setOnClickListener {
            staffViewModel.getStaffDate(++page)
        }
        prevPage.setOnClickListener {
            staffViewModel.getStaffDate(--page)
        }
    }
}