package com.example.arsipsurat.ui.admin.suratmasuk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.arsipsurat.R
import com.example.arsipsurat.ui.admin.suratmasuk.tambah.TambahSuratMasukActivity

class RiwayatSuratMasukActivity : AppCompatActivity() {

    private lateinit var cardViewTambahSuratMasuk: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riwayat_surat_masuk_admin)
        supportActionBar?.hide()

        // Set status bar color dan mode light
        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Button Kembali
        val btnKembali: ImageButton = findViewById(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }

        cardViewTambahSuratMasuk = findViewById(R.id.cardViewTambahSuratMasuk)

        cardViewTambahSuratMasuk.setOnClickListener {
            val intent = Intent(this, TambahSuratMasukActivity::class.java)
            startActivity(intent)
        }
    }
}