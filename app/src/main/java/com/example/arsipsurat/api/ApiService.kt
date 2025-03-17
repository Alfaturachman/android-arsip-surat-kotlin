package com.example.arsipsurat.api

import com.example.arsipsurat.model.Bagian
import com.example.arsipsurat.model.Surat
import com.example.arsipsurat.model.TambahSurat
import com.example.arsipsurat.model.TotalSurat
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    // Login
    @POST("login.php")
    fun loginUser(@Body body: RequestBody): Call<ResponseBody>

    @GET("get_total_surat.php")
    fun getTotalSurat(): Call<ApiResponse<TotalSurat>>

    // Admin Surat Masuk
    @GET("get_all_surat.php")
    fun getSuratMasuk(): Call<ApiResponse<List<Surat>>>

    @Headers("Content-Type: application/json")
    @POST("get_detail_surat.php")
    fun getDetailSurat(@Body request: Map<String, Int>): Call<ApiResponse<Surat>>

    @GET("get_bagian.php")
    fun getBagian(): Call<ApiResponse<List<Bagian>>>

    @Multipart
    @POST("post_surat_masuk.php")
    fun tambahSuratMasuk(
        @Part("kode_surat") kodeSurat: RequestBody,
        @Part("nomor_urut") nomorUrut: RequestBody,
        @Part("nomor_surat") nomorSurat: RequestBody,
        @Part("tanggal_masuk") tanggalMasuk: RequestBody,
        @Part("tanggal_surat") tanggalSurat: RequestBody,
        @Part("pengirim") pengirim: RequestBody,
        @Part("id_bagian_pengirim") idBagianPengirim: RequestBody,
        @Part("kepada") kepada: RequestBody,
        @Part("id_bagian_penerima") idBagianPenerima: RequestBody,
        @Part("perihal") perihal: RequestBody,
        @Part("disposisi1") disposisi1: RequestBody,
        @Part("tanggal_disposisi1") tanggalDisposisi1: RequestBody,
        @Part("disposisi2") disposisi2: RequestBody,
        @Part("tanggal_disposisi2") tanggalDisposisi2: RequestBody,
        @Part("disposisi3") disposisi3: RequestBody,
        @Part("tanggal_disposisi3") tanggalDisposisi3: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ApiResponse<TambahSurat>>

    @POST("update_surat.php")
    fun updateSuratMasuk(@Body request: Surat): Call<ApiResponse<Surat>>

    // Admin Surat Keluar
    @GET("get_all_surat_keluar.php")
    fun getSuratKeluar(): Call<ApiResponse<List<Surat>>>

    @Multipart
    @POST("post_surat_keluar.php")
    fun tambahSuratKeluar(
        @Part("kode_surat") kodeSurat: RequestBody,
        @Part("nomor_urut") nomorUrut: RequestBody,
        @Part("nomor_surat") nomorSurat: RequestBody,
        @Part("tanggal_masuk") tanggalMasuk: RequestBody,
        @Part("tanggal_surat") tanggalSurat: RequestBody,
        @Part("pengirim") pengirim: RequestBody,
        @Part("id_bagian_pengirim") idBagianPengirim: RequestBody,
        @Part("kepada") kepada: RequestBody,
        @Part("id_bagian_penerima") idBagianPenerima: RequestBody,
        @Part("perihal") perihal: RequestBody,
        @Part("disposisi1") disposisi1: RequestBody,
        @Part("tanggal_disposisi1") tanggalDisposisi1: RequestBody,
        @Part("disposisi2") disposisi2: RequestBody,
        @Part("tanggal_disposisi2") tanggalDisposisi2: RequestBody,
        @Part("disposisi3") disposisi3: RequestBody,
        @Part("tanggal_disposisi3") tanggalDisposisi3: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ApiResponse<TambahSurat>>

    // Bagian
}
