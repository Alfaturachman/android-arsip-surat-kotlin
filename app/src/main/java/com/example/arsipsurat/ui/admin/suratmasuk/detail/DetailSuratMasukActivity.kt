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

    private var idSurat: Int = -1
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

        idSurat = intent.getIntExtra("id_surat", -1)
        val kodeSurat = intent.getStringExtra("kode_surat")
        val nomorSurat = intent.getStringExtra("nomor_surat")
        val nomorUrut = intent.getStringExtra("nomor_urut")
        val tanggal = intent.getStringExtra("tanggal")
        val tanggalSurat = intent.getStringExtra("tanggal_surat")
        val penerima = intent.getStringExtra("penerima")
        val pengirim = intent.getStringExtra("pengirim")
        val perihal = intent.getStringExtra("perihal")
        val kategori = intent.getStringExtra("kategori")
        val status = intent.getStringExtra("status")
        val fileSurat = intent.getStringExtra("file_surat")
        val idBagianPengirim = intent.getIntExtra("id_bagian_pengirim", -1)
        val idBagianPenerima = intent.getIntExtra("id_bagian_penerima", -1)
        val disposisi1 = intent.getStringExtra("disposisi_1")
        val tanggalDisposisi1 = intent.getStringExtra("tanggal_disposisi_1")
        val disposisi2 = intent.getStringExtra("disposisi_2")
        val tanggalDisposisi2 = intent.getStringExtra("tanggal_disposisi_2")
        val disposisi3 = intent.getStringExtra("disposisi_3")
        val tanggalDisposisi3 = intent.getStringExtra("tanggal_disposisi_3")

        // Contoh mengatur teks secara dinamis
        tvTanggalSurat.text = "$tanggalSurat"
        tvKodeSurat.text = "$kodeSurat"
        tvNomorSurat.text = "$nomorSurat"
        tvPenerima.text = "$penerima"
        tvPengirim.text = "$pengirim"
        tvPerihal.text = "$perihal"
        tvNamaFilePdf.text = "$fileSurat"
        tvDisposisi1.text = disposisi1?.takeIf { it.isNotEmpty() } ?: "-"
        tvTanggalDisposisi1.text = tanggalDisposisi1?.takeIf { it.isNotEmpty() } ?: "-"
        tvDisposisi2.text = disposisi2?.takeIf { it.isNotEmpty() } ?: "-"
        tvTanggalDisposisi2.text = tanggalDisposisi2?.takeIf { it.isNotEmpty() } ?: "-"
        tvDisposisi3.text = disposisi3?.takeIf { it.isNotEmpty() } ?: "-"
        tvTanggalDisposisi3.text = tanggalDisposisi3?.takeIf { it.isNotEmpty() } ?: "-"
    }
}