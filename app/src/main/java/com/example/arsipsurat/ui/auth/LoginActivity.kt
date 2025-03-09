package com.example.arsipsurat.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.arsipsurat.MainActivity
import com.example.arsipsurat.R
import com.example.arsipsurat.api.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var buttonLogin: Button
    private lateinit var pagesRegister: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val sharedPreferences: SharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id_user", -1)

        if (userId != -1) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)
        buttonLogin = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val username = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (TextUtils.isEmpty(username)) {
                emailEditText.error = "Username is required"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                passwordEditText.error = "Password is required"
                return@setOnClickListener
            }

            loginUser(username, password)
        }
    }

    private fun loginUser(username: String, password: String) {
        val requestData = JSONObject()
        requestData.put("username", username)
        requestData.put("password", password)

        val body = RequestBody.create("application/json".toMediaTypeOrNull(), requestData.toString())

        val call = RetrofitClient.instance.loginUser(body)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val result = response.body()?.string()
                        Log.d("LoginActivity", "Response: $result")

                        val jsonResponse = JSONObject(result)

                        // Check if the response has the "status" and proceed with parsing
                        if (jsonResponse.getBoolean("status")) {
                            val userDetails = jsonResponse.getJSONObject("user_details")
                            val userLevel = jsonResponse.getString("level")
                            val userDetailId = userDetails.getString("id").toInt()
                            val userNama = userDetails.getString("nama")
                            val username = userDetails.getString("username")
                            val gambar = userDetails.getString("gambar")

                            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putInt("id_user", userDetailId)
                            editor.putString("level", userLevel)
                            editor.putString("username", username)
                            editor.putString("nama", userNama)
                            editor.putString("gambar", gambar)
                            editor.apply()

                            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login Gagal: ${jsonResponse.getString("error")}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        Log.e("LoginActivity", "JSON Error: ${e.message}")
                        Toast.makeText(this@LoginActivity, "Error Parsing Response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginActivity", "Response Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@LoginActivity, "Login Gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("LoginActivity", "Request Error: ${t.message}")
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}