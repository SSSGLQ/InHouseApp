package com.example.inhouseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private lateinit var loginButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val email = findViewById<EditText>(R.id.editTextEmail)
        val password = findViewById<EditText>(R.id.editTextPassword)
        loginButton = findViewById<Button>(R.id.buttonLogin)
        val error = findViewById<TextView>(R.id.textViewError)
        loginButton.setOnClickListener{
            setButtonEnabled(false)
            error.visibility = View.GONE
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            if (isValid(emailText, passwordText)) {
                val loginData = retrofitBuilder.login(5, LoginRequest(emailText, passwordText))
                progress.visibility = View.VISIBLE
                loginData.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            // if the API call is a success then these operations are executed
                            val tokenResponse = response.body()
                            if (tokenResponse?.token != null) {
                                val token = tokenResponse.token
                                // process the token, jump to staff list page
                                navigateToStaffPage(token)
                            } else {
                                val errorMessage = tokenResponse?.error ?: "Unknown error"
                                // display error message
                                error.text = errorMessage
                                error.visibility = View.VISIBLE
                            }
                        } else {
                            // if the API call is a failure then these operations are executed
                            error.text = getString(R.string.login_fail)
                            error.visibility = View.VISIBLE
                        }
                        progress.visibility = View.GONE
                        setButtonEnabled(true)
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        // if the API call is a failure then this method is executed
                        Log.d(TAG, "onFailure: " + t.message)
                        error.text = getString(R.string.login_fail)
                        error.visibility = View.VISIBLE
                        progress.visibility = View.GONE
                        setButtonEnabled(true)
                    }
                })
            } else {
                error.text = getString(R.string.invalid_error)
                error.visibility = View.VISIBLE
                setButtonEnabled(true)
            }
        }

    }

    private fun isValid(email: String, password: String): Boolean {
        // if the email and password are both valid
        return email.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setButtonEnabled(enabled: Boolean) {
        loginButton.isEnabled = enabled
        if (enabled) {
            // recover color of button
            loginButton.alpha = 1.0f
        } else {
            // set color of button to gray
            loginButton.alpha = 0.5f
        }
    }

    private fun navigateToStaffPage(token: String) {
        val intent = Intent(this, StaffListActivity::class.java)
        intent.putExtra("TOKEN", token)
        startActivity(intent)
        finish()
    }
}