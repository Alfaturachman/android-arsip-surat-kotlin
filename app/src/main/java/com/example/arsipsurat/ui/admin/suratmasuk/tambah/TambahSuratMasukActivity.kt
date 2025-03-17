package com.example.arsipsurat.ui.admin.suratmasuk.tambah

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.arsipsurat.R
import com.example.arsipsurat.api.ApiResponse
import com.example.arsipsurat.api.ApiService
import com.example.arsipsurat.api.RetrofitClient
import com.example.arsipsurat.model.Bagian
import com.example.arsipsurat.model.TambahSurat
import com.example.arsipsurat.ui.admin.suratmasuk.RiwayatSuratMasukActivity
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class TambahSuratMasukActivity : AppCompatActivity() {

    private lateinit var selectPdfLauncher: ActivityResultLauncher<Intent>
    private lateinit var tvNamaFilePdf: TextView
    private lateinit var etTanggalMasuk: TextInputEditText
    private lateinit var etTanggalSurat: TextInputEditText
    private lateinit var etTanggalDisposisi1: TextInputEditText
    private lateinit var etTanggalDisposisi2: TextInputEditText
    private lateinit var etTanggalDisposisi3: TextInputEditText
    private lateinit var spinnerPengirim: Spinner
    private lateinit var spinnerKepada: Spinner
    private var bagianList: List<Pair<String, Int>> = emptyList()
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_surat_masuk_admin)
        supportActionBar?.hide()

        // Set status bar color dan mode light
        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Button Kembali
        val btnKembali: ImageButton = findViewById(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }

        // Initialize views
        tvNamaFilePdf = findViewById(R.id.tvNamaFilePdf)
        etTanggalMasuk = findViewById(R.id.etTanggalMasuk)
        etTanggalSurat = findViewById(R.id.etTanggalSurat)
        etTanggalDisposisi1 = findViewById(R.id.etTanggalDisposisi1)
        etTanggalDisposisi2 = findViewById(R.id.etTanggalDisposisi2)
        etTanggalDisposisi3 = findViewById(R.id.etTanggalDisposisi3)

        // Set up DatePicker for Tanggal Masuk
        etTanggalMasuk.setOnClickListener {
            showDateTimePicker(etTanggalMasuk, "2025-05-01 20:33:00")
        }

        // Set up DatePicker for Tanggal Surat
        etTanggalSurat.setOnClickListener {
            showDatePicker(etTanggalSurat, "2025-05-01")
        }

        // Set up DatePicker for Tanggal Disposisi 1
        etTanggalDisposisi1.setOnClickListener {
            showDateTimePicker(etTanggalDisposisi1, "2025-01-09 08:02:53")
        }

        // Set up DatePicker for Tanggal Disposisi 2
        etTanggalDisposisi2.setOnClickListener {
            showDateTimePicker(etTanggalDisposisi2, "2025-01-09 08:02:53")
        }

        // Set up DatePicker for Tanggal Disposisi 3
        etTanggalDisposisi3.setOnClickListener {
            showDateTimePicker(etTanggalDisposisi3, "2025-01-09 08:02:53")
        }

        selectPdfLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    val fileName = getFileName(it)
                    tvNamaFilePdf.text = fileName

                    // Log informasi file
                    Log.d("PDF_UPLOAD", "File URI: $uri")
                    Log.d("PDF_UPLOAD", "File Name: $fileName")
                }
            } else {
                Log.d("PDF_UPLOAD", "File selection canceled")
            }
        }

        spinnerPengirim = findViewById(R.id.spinnerPengirim)
        spinnerKepada = findViewById(R.id.spinnerKepada)

        getBagianData()

        val buttonTambah = findViewById<com.google.android.material.button.MaterialButton>(R.id.buttonTambah)
        buttonTambah.setOnClickListener {
            simpanSuratMasuk()
        }
    }

    fun selectPdf(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        selectPdfLauncher.launch(intent)
    }

    private fun getFileName(uri: Uri): String {
        var result = "Unknown"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = cursor.getString(index)
                }
            }
        }
        return result
    }

    private fun getBagianData() {
        apiService = RetrofitClient.instance
        apiService.getBagian().enqueue(object : Callback<ApiResponse<List<Bagian>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Bagian>>>,
                response: Response<ApiResponse<List<Bagian>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        if (apiResponse.status) {
                            val bagianList = apiResponse.data ?: emptyList()
                            val bagianPairs = bagianList.map { Pair(it.nama_bagian, it.id_bagian) }

                            setupSpinner(spinnerPengirim, bagianPairs)
                            setupSpinner(spinnerKepada, bagianPairs)
                        } else {
                            showToast("Gagal mengambil data")
                        }
                    } ?: showToast("Response body null")
                } else {
                    showToast("Response tidak berhasil")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Bagian>>>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupSpinner(spinner: Spinner, dataList: List<Pair<String, Int>>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dataList.map { it.first })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedName = dataList[position].first
                val selectedId = dataList[position].second
                Log.d("Spinner", "${spinner.id}: $selectedName (ID: $selectedId)")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun simpanSuratMasuk() {
        val etKodeSurat = findViewById<TextInputEditText>(R.id.etKodeSurat)
        val etNomorUrut = findViewById<TextInputEditText>(R.id.etNomorUrut)
        val etNomorSurat = findViewById<TextInputEditText>(R.id.etNomorSurat)
        val etPerihal = findViewById<TextInputEditText>(R.id.etPerihal)
        val etDisposisi1 = findViewById<TextInputEditText>(R.id.etDisposisi1)
        val etDisposisi2 = findViewById<TextInputEditText>(R.id.etDisposisi2)
        val etDisposisi3 = findViewById<TextInputEditText>(R.id.etDisposisi3)
        val etTanggalMasuk = findViewById<TextInputEditText>(R.id.etTanggalMasuk)
        val etTanggalSurat = findViewById<TextInputEditText>(R.id.etTanggalSurat)
        val etTanggalDisposisi1 = findViewById<TextInputEditText>(R.id.etTanggalDisposisi1)
        val etTanggalDisposisi2 = findViewById<TextInputEditText>(R.id.etTanggalDisposisi2)
        val etTanggalDisposisi3 = findViewById<TextInputEditText>(R.id.etTanggalDisposisi3)
        // Ambil nilai dari Spinner
        val selectedPengirimPos = spinnerPengirim.selectedItemPosition
        val selectedKepadaPos = spinnerKepada.selectedItemPosition

        // Ambil nama dan ID bagian berdasarkan posisi yang dipilih
        val bagianPengirim = if (selectedPengirimPos >= 0) bagianList[selectedPengirimPos] else null
        val bagianKepada = if (selectedKepadaPos >= 0) bagianList[selectedKepadaPos] else null

        // Simpan nama dan ID bagian ke dalam variabel yang akan dikirim ke API
        val namaPengirim = bagianPengirim?.first ?: "null"
        val idBagianPengirim = bagianPengirim?.second ?: -1
        val namaKepada = bagianKepada?.first ?: "null"
        val idBagianPenerima = bagianKepada?.second ?: -1

        val kodeSurat = etKodeSurat.text.toString().trim()
        val nomorUrut = etNomorUrut.text.toString().trim()
        val nomorSurat = etNomorSurat.text.toString().trim()
        val perihal = etPerihal.text.toString().trim()
        val disposisi1 = etDisposisi1.text.toString().trim()
        val disposisi2 = etDisposisi2.text.toString().trim()
        val disposisi3 = etDisposisi3.text.toString().trim()

        // Validasi input (jika ada yang kosong, tampilkan pesan error)
        if (kodeSurat.isEmpty() || nomorUrut.isEmpty() || nomorSurat.isEmpty() ||
            perihal.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Format untuk tanggal dengan waktu (tanggal_masuk dan tanggal_disposisi)
        val inputFormatterDateTime = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss")
        val outputFormatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Format untuk tanggal saja (tanggal_surat)
        val inputFormatterDate = DateTimeFormatter.ofPattern("yyyy-M-d")
        val outputFormatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Format tanggal_masuk
        val formattedTanggalMasuk = etTanggalMasuk.text.toString().trim().let {
            LocalDateTime.parse(it, inputFormatterDateTime).format(outputFormatterDateTime)
        }

        // Format tanggal_surat
        val formattedTanggalSurat = etTanggalSurat.text.toString().trim().let {
            LocalDate.parse(it, inputFormatterDate).format(outputFormatterDate)
        }

        // Format tanggal_disposisi1 (jika ada)
        val formattedTanggalDisposisi1 = etTanggalDisposisi1.text.toString().trim().takeIf { it.isNotEmpty() }?.let {
            LocalDateTime.parse(it, inputFormatterDateTime).format(outputFormatterDateTime)
        } ?: ""

        // Format tanggal_disposisi2 (jika ada)
        val formattedTanggalDisposisi2 = etTanggalDisposisi2.text.toString().trim().takeIf { it.isNotEmpty() }?.let {
            LocalDateTime.parse(it, inputFormatterDateTime).format(outputFormatterDateTime)
        } ?: ""

        // Format tanggal_disposisi3 (jika ada)
        val formattedTanggalDisposisi3 = etTanggalDisposisi3.text.toString().trim().takeIf { it.isNotEmpty() }?.let {
            LocalDateTime.parse(it, inputFormatterDateTime).format(outputFormatterDateTime)
        } ?: ""

        // Buat objek request untuk API
        val request = TambahSurat(
            kode_surat = kodeSurat,
            nomor_urut = nomorUrut,
            nomor_surat = nomorSurat,
            tanggal_masuk = formattedTanggalMasuk,
            tanggal_surat = formattedTanggalSurat,
            pengirim = namaPengirim,  // Nama bagian dari spinner
            id_bagian_pengirim = idBagianPengirim,  // ID bagian dari spinner
            kepada = namaKepada,  // Nama bagian dari spinner
            id_bagian_penerima = idBagianPenerima,  // ID bagian dari spinner
            perihal = perihal,
            disposisi1 = disposisi1,
            tanggal_disposisi1 = formattedTanggalDisposisi1,
            disposisi2 = disposisi2,
            tanggal_disposisi2 = formattedTanggalDisposisi2,
            disposisi3 = disposisi3,
            tanggal_disposisi3 = formattedTanggalDisposisi3
        )

        Log.d("TambahSuratMasuk", "Request yang dikirim: $request")

        // Kirim data ke API menggunakan Retrofit
        RetrofitClient.instance.tambahSurat(request).enqueue(object :
            Callback<ApiResponse<TambahSurat>> {
            override fun onResponse(
                call: Call<ApiResponse<TambahSurat>>,
                response: Response<ApiResponse<TambahSurat>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()

                    Log.d("TambahSuratMasuk", "Response berhasil diterima: $result")

                    if (result != null && result.status) {
                        Log.d("TambahSuratMasuk", "Surat Masuk berhasil disimpan!")

                        Toast.makeText(this@TambahSuratMasukActivity, "Surat Masuk berhasil disimpan!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@TambahSuratMasukActivity,
                            RiwayatSuratMasukActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("TambahSuratMasuk", "Gagal menyimpan surat masuk. Error: $errorBody")
                        Toast.makeText(this@TambahSuratMasukActivity, "Gagal menyimpan surat masuk!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("TambahSuratMasuk", "Response error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@TambahSuratMasukActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<TambahSurat>>, t: Throwable) {
                Log.e("TambahSuratMasuk", "Gagal menghubungi server: ${t.message}", t)
                Toast.makeText(this@TambahSuratMasukActivity, "Gagal menghubungi server!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDatePicker(editText: TextInputEditText, defaultDate: String) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            editText.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showDateTimePicker(editText: TextInputEditText, defaultDateTime: String) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val datePickerDialog = DatePickerDialog(this, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val timePickerDialog = TimePickerDialog(this, { _: TimePicker, hourOfDay: Int, minute: Int ->
                val selectedDateTime = "$year-${month + 1}-$dayOfMonth $hourOfDay:$minute:00"
                editText.setText(selectedDateTime)
            }, hour, minute, true)

            timePickerDialog.show()
        }, year, month, day)

        datePickerDialog.show()
    }
}