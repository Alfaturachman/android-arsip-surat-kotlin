package com.example.arsipsurat.api

import com.example.arsipsurat.model.Bagian
import com.example.arsipsurat.model.Surat
import com.example.arsipsurat.model.TambahSurat
import com.example.arsipsurat.model.TotalSurat
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

    @GET("get_total_surat.php")
    fun getTotalSurat(): Call<ApiResponse<TotalSurat>>

    // Admin
    @GET("get_all_surat.php")
    fun getSuratMasuk(): Call<ApiResponse<List<Surat>>>

    @Headers("Content-Type: application/json")
    @POST("get_detail_surat.php")
    fun getDetailSurat(@Body request: Map<String, Int>): Call<ApiResponse<Surat>>

    @GET("get_bagian.php")
    fun getBagian(): Call<ApiResponse<List<Bagian>>>

    @POST("post_surat_masuk.php")
    fun tambahSurat(@Body request: TambahSurat): Call<ApiResponse<TambahSurat>>

    // Bagian

}
