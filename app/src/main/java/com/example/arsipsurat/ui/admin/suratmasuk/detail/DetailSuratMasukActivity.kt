package com.example.arsipsurat.ui.admin.suratmasuk.detail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.arsipsurat.R
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout

class DetailSuratMasukActivity : AppCompatActivity() {

    private lateinit var titleText: TextView
    private lateinit var tvTanggalSurat: TextView
    private lateinit var tvKodeSurat: TextView
    private lateinit var tvNomorSurat: TextView
    private lateinit var tvPenerima: TextView
    private lateinit var tvPengirim: TextView
    private lateinit var tvPerihal: TextView
    private lateinit var ivPdfIcon: ImageView
    private lateinit var tvNamaFilePdf: TextView
    private lateinit var layoutFilePdf: LinearLayout
    private lateinit var layoutDisposisi: LinearLayout
    private lateinit var tvDisposisi1: TextView
    private lateinit var tvTanggalDisposisi1: TextView
    private lateinit var tvDisposisi2: TextView
    private lateinit var tvTanggalDisposisi2: TextView
    private lateinit var tvDisposisi3: TextView
    private lateinit var tvTanggalDisposisi3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_surat_masuk_admin)
        supportActionBar?.hide()

        // Set status bar color dan mode light
        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Button Kembali
        val btnKembali: ImageButton = findViewById(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }

        titleText = findViewById(R.id.title_text)
        tvTanggalSurat = findViewById(R.id.tvTanggalSurat)
        tvKodeSurat = findViewById(R.id.tvKodeSurat)
        tvNomorSurat = findViewById(R.id.tvNomorSurat)
        tvPenerima = findViewById(R.id.tvPenerima)
        tvPengirim = findViewById(R.id.tvPengirim)
        tvPerihal = findViewById(R.id.tvPerihal)
        ivPdfIcon = findViewById(R.id.ivPdfIcon)
        tvNamaFilePdf = findViewById(R.id.tvNamaFilePdf)
        layoutFilePdf = findViewById(R.id.layoutFilePdf)
        layoutDisposisi = findViewById(R.id.layoutDisposisi)
        tvDisposisi1 = findViewById(R.id.tvDisposisi1)
        tvTanggalDisposisi1 = findViewById(R.id.tvTanggalDisposisi1)
        tvDisposisi2 = findViewById(R.id.tvDisposisi2)
        tvTanggalDisposisi2 = findViewById(R.id.tvTanggalDisposisi2)
        tvDisposisi3 = findViewById(R.id.tvDisposisi3)
        tvTanggalDisposisi3 = findViewById(R.id.tvTanggalDisposisi3)

        // Contoh mengatur teks secara dinamis
        tvTanggalSurat.text = "2023-10-20"
        tvKodeSurat.text = "1234"
        tvNomorSurat.text = "5678"
        tvPenerima.text = "Ka. Balai PSDA"
        tvPengirim.text = "Ka. Bid PPT"
        tvPerihal.text = "Kasus ini mengenai sengketa tanah antara dua pihak."
        tvNamaFilePdf.text = "surat_masuk_baru.pdf"
        tvDisposisi1.text = "Disposisi 1 Baru"
        tvTanggalDisposisi1.text = "2023-10-20"
        tvDisposisi2.text = "Disposisi 2 Baru"
        tvTanggalDisposisi2.text = "2023-10-21"
        tvDisposisi3.text = "Disposisi 3 Baru"
        tvTanggalDisposisi3.text = "2023-10-22"
    }
}