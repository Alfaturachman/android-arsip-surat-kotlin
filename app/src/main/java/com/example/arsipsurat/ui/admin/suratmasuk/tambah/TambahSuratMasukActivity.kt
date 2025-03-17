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
import android.widget.EditText
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
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
    private var selectedPdfUri: Uri? = null
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

        // Set up DatePicker for Tanggal Surat (hanya tanggal)
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
                    selectedPdfUri = it  // Simpan URI untuk dikirim ke API
                    val fileName = getFileName(it)
                    tvNamaFilePdf.text = fileName

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

    private fun getFilePart(uri: Uri): MultipartBody.Part? {
        val fileDescriptor = contentResolver.openFileDescriptor(uri, "r") ?: return null
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val file = File(cacheDir, getFileName(uri))
        file.outputStream().use { output -> inputStream.copyTo(output) }

        val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file_surat", file.name, requestFile)
    }

    private fun simpanSuratMasuk() {
        // Ambil nilai dari input fields
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
        val spinnerPengirim = findViewById<Spinner>(R.id.spinnerPengirim)
        val spinnerKepada = findViewById<Spinner>(R.id.spinnerKepada)
        val selectedPengirimPos = spinnerPengirim.selectedItemPosition
        val selectedKepadaPos = spinnerKepada.selectedItemPosition

        // Ambil nama dan ID bagian berdasarkan posisi yang dipilih
        val bagianPengirim = if (selectedPengirimPos in bagianList.indices) bagianList[selectedPengirimPos] else null
        val bagianKepada = if (selectedKepadaPos in bagianList.indices) bagianList[selectedKepadaPos] else null

        Log.d("TambahSuratMasuk", "Bagian List Size: ${bagianList.size}")
        Log.d("TambahSuratMasuk", "Selected Pengirim Pos: $selectedPengirimPos")
        Log.d("TambahSuratMasuk", "Selected Kepada Pos: $selectedKepadaPos")

        // Simpan nama dan ID bagian ke dalam variabel yang akan dikirim ke API
        val namaPengirim = bagianPengirim?.first ?: ""
        val idBagianPengirim = bagianPengirim?.second?.toString() ?: ""
        val namaKepada = bagianKepada?.first ?: ""
        val idBagianPenerima = bagianKepada?.second?.toString() ?: ""

        // Ambil nilai dari input fields
        val kodeSurat = etKodeSurat.text.toString().trim()
        val nomorUrut = etNomorUrut.text.toString().trim()
        val nomorSurat = etNomorSurat.text.toString().trim()
        val perihal = etPerihal.text.toString().trim()
        val disposisi1 = etDisposisi1.text.toString().trim()
        val disposisi2 = etDisposisi2.text.toString().trim()
        val disposisi3 = etDisposisi3.text.toString().trim()
        val tanggalMasuk = etTanggalMasuk.text.toString().trim()
        val tanggalSurat = etTanggalSurat.text.toString().trim()
        val tanggalDisposisi1 = etTanggalDisposisi1.text.toString().trim()
        val tanggalDisposisi2 = etTanggalDisposisi2.text.toString().trim()
        val tanggalDisposisi3 = etTanggalDisposisi3.text.toString().trim()

        // Validasi input (jika ada yang kosong, tampilkan pesan error)
        if (kodeSurat.isEmpty() || nomorUrut.isEmpty() || nomorSurat.isEmpty() || perihal.isEmpty() || tanggalMasuk.isEmpty() || tanggalSurat.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedPdfUri == null) {
            Toast.makeText(this, "Silakan pilih file PDF terlebih dahulu!", Toast.LENGTH_SHORT).show()
            return
        }

        val filePart = getFilePart(selectedPdfUri!!) ?: return

        // Format untuk tanggal dengan waktu (tanggal_masuk dan tanggal_disposisi)
        val inputFormatterDateTime = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss")
        val outputFormatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Format untuk tanggal saja (tanggal_surat)
        val inputFormatterDate = DateTimeFormatter.ofPattern("yyyy-M-d")
        val outputFormatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Format tanggal_masuk
        val formattedTanggalMasuk = try {
            LocalDateTime.parse(tanggalMasuk, inputFormatterDateTime).format(outputFormatterDateTime)
        } catch (e: Exception) {
            Log.e("TambahSuratMasuk", "Error parsing tanggal masuk: ${e.message}")
            Toast.makeText(this, "Format tanggal masuk tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        // Format tanggal_surat
        val formattedTanggalSurat = try {
            LocalDate.parse(tanggalSurat, inputFormatterDate).format(outputFormatterDate)
        } catch (e: Exception) {
            Log.e("TambahSuratMasuk", "Error parsing tanggal surat: ${e.message}")
            Toast.makeText(this, "Format tanggal surat tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        // Format tanggal_disposisi1 (jika ada)
        val formattedTanggalDisposisi1 = tanggalDisposisi1.takeIf { it.isNotEmpty() }?.let {
            try {
                LocalDateTime.parse(it, inputFormatterDateTime).format(outputFormatterDateTime)
            } catch (e: Exception) {
                Log.e("TambahSuratMasuk", "Error parsing tanggal disposisi 1: ${e.message}")
                Toast.makeText(this, "Format tanggal disposisi 1 tidak valid!", Toast.LENGTH_SHORT).show()
                return
            }
        } ?: ""

        // Format tanggal_disposisi2 (jika ada)
        val formattedTanggalDisposisi2 = tanggalDisposisi2.takeIf { it.isNotEmpty() }?.let {
            try {
                LocalDateTime.parse(it, inputFormatterDateTime).format(outputFormatterDateTime)
            } catch (e: Exception) {
                Log.e("TambahSuratMasuk", "Error parsing tanggal disposisi 2: ${e.message}")
                Toast.makeText(this, "Format tanggal disposisi 2 tidak valid!", Toast.LENGTH_SHORT).show()
                return
            }
        } ?: ""

        // Format tanggal_disposisi3 (jika ada)
        val formattedTanggalDisposisi3 = tanggalDisposisi3.takeIf { it.isNotEmpty() }?.let {
            try {
                LocalDateTime.parse(it, inputFormatterDateTime).format(outputFormatterDateTime)
            } catch (e: Exception) {
                Log.e("TambahSuratMasuk", "Error parsing tanggal disposisi 3: ${e.message}")
                Toast.makeText(this, "Format tanggal disposisi 3 tidak valid!", Toast.LENGTH_SHORT).show()
                return
            }
        } ?: ""

        // Ubah semua string menjadi RequestBody
        val kodeSuratBody = kodeSurat.toRequestBody("text/plain".toMediaTypeOrNull())
        val nomorUrutBody = nomorUrut.toRequestBody("text/plain".toMediaTypeOrNull())
        val nomorSuratBody = nomorSurat.toRequestBody("text/plain".toMediaTypeOrNull())
        val tanggalMasukBody = formattedTanggalMasuk.toRequestBody("text/plain".toMediaTypeOrNull())
        val tanggalSuratBody = formattedTanggalSurat.toRequestBody("text/plain".toMediaTypeOrNull())
        val pengirimBody = namaPengirim.toRequestBody("text/plain".toMediaTypeOrNull())
        val idBagianPengirimBody = idBagianPengirim.toRequestBody("text/plain".toMediaTypeOrNull())
        val kepadaBody = namaKepada.toRequestBody("text/plain".toMediaTypeOrNull())
        val idBagianPenerimaBody = idBagianPenerima.toRequestBody("text/plain".toMediaTypeOrNull())
        val perihalBody = perihal.toRequestBody("text/plain".toMediaTypeOrNull())
        val disposisi1Body = disposisi1.toRequestBody("text/plain".toMediaTypeOrNull())
        val tanggalDisposisi1Body = formattedTanggalDisposisi1.toRequestBody("text/plain".toMediaTypeOrNull())
        val disposisi2Body = disposisi2.toRequestBody("text/plain".toMediaTypeOrNull())
        val tanggalDisposisi2Body = formattedTanggalDisposisi2.toRequestBody("text/plain".toMediaTypeOrNull())
        val disposisi3Body = disposisi3.toRequestBody("text/plain".toMediaTypeOrNull())
        val tanggalDisposisi3Body = formattedTanggalDisposisi3.toRequestBody("text/plain".toMediaTypeOrNull())

        Log.d("TambahSuratMasuk", "Kode Surat: $kodeSurat")
        Log.d("TambahSuratMasuk", "Nomor Urut: $nomorUrut")
        Log.d("TambahSuratMasuk", "Nomor Surat: $nomorSurat")
        Log.d("TambahSuratMasuk", "Tanggal Masuk: $formattedTanggalMasuk")
        Log.d("TambahSuratMasuk", "Tanggal Surat: $formattedTanggalSurat")
        Log.d("TambahSuratMasuk", "Pengirim: $namaPengirim")
        Log.d("TambahSuratMasuk", "ID Pengirim: $idBagianPengirim")
        Log.d("TambahSuratMasuk", "Kepada: $namaKepada")
        Log.d("TambahSuratMasuk", "ID Penerima: $idBagianPenerima")
        Log.d("TambahSuratMasuk", "Perihal: $perihal")
        Log.d("TambahSuratMasuk", "Disposisi 1: $disposisi1")
        Log.d("TambahSuratMasuk", "Tanggal Disposisi 1: $formattedTanggalDisposisi1")
        Log.d("TambahSuratMasuk", "Disposisi 2: $disposisi2")
        Log.d("TambahSuratMasuk", "Tanggal Disposisi 2: $formattedTanggalDisposisi2")
        Log.d("TambahSuratMasuk", "Disposisi 3: $disposisi3")
        Log.d("TambahSuratMasuk", "Tanggal Disposisi 3: $formattedTanggalDisposisi3")

        // Kirim data ke API menggunakan Retrofit
        RetrofitClient.instance.tambahSurat(
            kodeSuratBody,
            nomorUrutBody,
            nomorSuratBody,
            tanggalMasukBody,
            tanggalSuratBody,
            pengirimBody,
            idBagianPengirimBody,
            kepadaBody,
            idBagianPenerimaBody,
            perihalBody,
            disposisi1Body,
            tanggalDisposisi1Body,
            disposisi2Body,
            tanggalDisposisi2Body,
            disposisi3Body,
            tanggalDisposisi3Body,
            filePart
        ).enqueue(object : Callback<ApiResponse<TambahSurat>> {
            override fun onResponse(
                call: Call<ApiResponse<TambahSurat>>,
                response: Response<ApiResponse<TambahSurat>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null && result.status) {
                        Toast.makeText(this@TambahSuratMasukActivity, "Surat Masuk berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@TambahSuratMasukActivity, RiwayatSuratMasukActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("TambahSuratMasuk", "Gagal menyimpan surat masuk. Error: $errorBody")
                        Toast.makeText(this@TambahSuratMasukActivity, "Gagal menyimpan surat masuk!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("TambahSuratMasuk", "Response error: ${response.code()} - ${response.message()}. Error body: $errorBody")
                    Toast.makeText(this@TambahSuratMasukActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<TambahSurat>>, t: Throwable) {
                Log.e("TambahSuratMasuk", "Gagal menghubungi server: ${t.message}", t)
                Toast.makeText(this@TambahSuratMasukActivity, "Gagal menghubungi server!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showDatePicker(editText: EditText, defaultDate: String) {
        val calendar = Calendar.getInstance()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val defaultDateParsed = LocalDate.parse(defaultDate, dateFormatter)

        calendar.set(
            defaultDateParsed.year,
            defaultDateParsed.monthValue - 1, // Bulan di Calendar dimulai dari 0
            defaultDateParsed.dayOfMonth
        )

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                val formattedDate = selectedDate.format(dateFormatter)
                editText.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun showDateTimePicker(editText: EditText, defaultDateTime: String) {
        val calendar = Calendar.getInstance()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val defaultDateTimeParsed = LocalDateTime.parse(defaultDateTime, dateTimeFormatter)

        calendar.set(
            defaultDateTimeParsed.year,
            defaultDateTimeParsed.monthValue - 1, // Bulan di Calendar dimulai dari 0
            defaultDateTimeParsed.dayOfMonth,
            defaultDateTimeParsed.hour,
            defaultDateTimeParsed.minute
        )

        val dateTimePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        val selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute)
                        val formattedDateTime = selectedDateTime.format(dateTimeFormatter)
                        editText.setText(formattedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dateTimePickerDialog.show()
    }
}