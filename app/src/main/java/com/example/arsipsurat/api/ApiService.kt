package com.example.arsipsurat.api

import com.example.arsipsurat.model.Surat
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    // Login
    @POST("login.php")
    fun loginUser(@Body body: RequestBody): Call<ResponseBody>

    // Admin
    @GET("get_all_surat_masuk.php")
    fun getSuratMasuk(): Call<ApiResponse<List<Surat>>>

    // Bagian

}
