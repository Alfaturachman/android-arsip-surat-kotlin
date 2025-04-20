package com.example.arsipsurat.ui.bagian.suratkeluar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arsipsurat.R
import com.example.arsipsurat.api.ApiResponse
import com.example.arsipsurat.api.RetrofitClient
import com.example.arsipsurat.model.Surat
import com.example.arsipsurat.ui.bagian.suratkeluar.RiwayatSuratKeluarAdapter
import com.example.arsipsurat.ui.bagian.suratkeluar.tambah.TambahSuratKeluarActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatSuratKeluarActivity : AppCompatActivity() {

    private var idUser: Int = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RiwayatSuratKeluarAdapter
    private lateinit var cardViewTambahSuratKeluar: CardView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riwayat_surat_keluar_bagian)
        supportActionBar?.hide()

        // Set status bar color dan mode light
        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Button Kembali
        val btnKembali: ImageButton = findViewById(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }

        cardViewTambahSuratKeluar = findViewById(R.id.cardViewTambahSuratKeluar)

        cardViewTambahSuratKeluar.setOnClickListener {
            val intent = Intent(this, TambahSuratKeluarActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerViewRiwayatSuratKeluar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = RiwayatSuratKeluarAdapter(emptyList())
        recyclerView.adapter = adapter

        idUser = getUserIdFromSharedPreferences()

        fetchRiwayatPelaporan(idUser)
    }

    private fun fetchRiwayatPelaporan(idUser: Int) {
        val requestBody = hashMapOf("id_user" to idUser)

        Log.d("RiwayatMedia", "Mengirim request ke server dengan body: $requestBody")

        val call = RetrofitClient.instance.getSuratKeluarBagian(requestBody)
        call.enqueue(object : Callback<ApiResponse<List<Surat>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Surat>>>,
                response: Response<ApiResponse<List<Surat>>>
            ) {
                Log.d("RiwayatPelapor", "Response diterima dengan kode: ${response.code()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == true) {
                        Log.d("RiwayatPelapor", "Data berhasil diterima: ${responseBody.data}")
                        responseBody.data?.let {
                            adapter.updateData(it)
                        }
                    } else {
                        Log.e("RiwayatPelapor", "Gagal mendapatkan data: ${responseBody?.message}")
                    }
                } else {
                    Log.e("RiwayatPelapor", "Request gagal dengan kode: ${response.code()}, pesan: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Surat>>>, t: Throwable) {
                Log.e("RiwayatPelapor", "Gagal menghubungi server: ${t.localizedMessage}", t)
            }
        })
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("id_user", -1)
    }
}