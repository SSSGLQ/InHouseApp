package com.example.inhouseapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val email = findViewById<EditText>(R.id.editTextEmail)
        val password = findViewById<EditText>(R.id.editTextPassword)
        loginButton = findViewById<Button>(R.id.buttonLogin)
        val error = findViewById<TextView>(R.id.textViewError)

        // observe LiveData, refresh interface
        loginViewModel.loginResult.observe(this) { result ->
            if (result.first) {
                navigateToStaffPage(result.second)
            } else {
                error.text = result.second
                error.visibility = View.VISIBLE
            }
        }

        loginViewModel.isLoading.observe(this) { loading ->
            progress.visibility = if (loading) View.VISIBLE else View.GONE
            if (!loading) setButtonEnabled(true)
        }

        loginButton.setOnClickListener{
            setButtonEnabled(false)
            error.visibility = View.GONE
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            if (isValid(emailText, passwordText)) {
                progress.visibility = View.VISIBLE
                loginViewModel.loginUser(emailText, passwordText)
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