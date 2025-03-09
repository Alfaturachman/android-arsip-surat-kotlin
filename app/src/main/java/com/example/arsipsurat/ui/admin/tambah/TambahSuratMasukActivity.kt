package com.example.arsipsurat.ui.admin.tambah

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.arsipsurat.R
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class TambahSuratMasukActivity : AppCompatActivity() {

    private lateinit var etTanggalMasuk: TextInputEditText
    private lateinit var etTanggalSurat: TextInputEditText
    private lateinit var etTanggalDisposisi1: TextInputEditText
    private lateinit var etTanggalDisposisi2: TextInputEditText
    private lateinit var etTanggalDisposisi3: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_surat_masuk_admin)

        // Initialize views
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

        // Simpan semua nilai EditText ke log
        val buttonTambah = findViewById<com.google.android.material.button.MaterialButton>(R.id.buttonTambah)
        buttonTambah.setOnClickListener {
            logEditTextValues()
        }
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

    private fun logEditTextValues() {
        val etKodeSurat = findViewById<TextInputEditText>(R.id.etKodeSurat)
        val etNomorUrut = findViewById<TextInputEditText>(R.id.etNomorUrut)
        val etNomorSurat = findViewById<TextInputEditText>(R.id.etNomorSurat)
        val etPengirim = findViewById<TextInputEditText>(R.id.etPengirim)
        val etKepada = findViewById<TextInputEditText>(R.id.etKepada)
        val etPerihal = findViewById<TextInputEditText>(R.id.etPerihal)
        val etDisposisi1 = findViewById<TextInputEditText>(R.id.etDisposisi1)
        val etDisposisi2 = findViewById<TextInputEditText>(R.id.etDisposisi2)
        val etDisposisi3 = findViewById<TextInputEditText>(R.id.etDisposisi3)

        Log.d("TambahSuratMasuk", "Kode Surat: ${etKodeSurat.text}")
        Log.d("TambahSuratMasuk", "Nomor Urut: ${etNomorUrut.text}")
        Log.d("TambahSuratMasuk", "Nomor Surat: ${etNomorSurat.text}")
        Log.d("TambahSuratMasuk", "Tanggal Masuk: ${etTanggalMasuk.text}")
        Log.d("TambahSuratMasuk", "Tanggal Surat: ${etTanggalSurat.text}")
        Log.d("TambahSuratMasuk", "Pengirim: ${etPengirim.text}")
        Log.d("TambahSuratMasuk", "Kepada: ${etKepada.text}")
        Log.d("TambahSuratMasuk", "Perihal: ${etPerihal.text}")
        Log.d("TambahSuratMasuk", "Disposisi 1: ${etDisposisi1.text}")
        Log.d("TambahSuratMasuk", "Tanggal Disposisi 1: ${etTanggalDisposisi1.text}")
        Log.d("TambahSuratMasuk", "Disposisi 2: ${etDisposisi2.text}")
        Log.d("TambahSuratMasuk", "Tanggal Disposisi 2: ${etTanggalDisposisi2.text}")
        Log.d("TambahSuratMasuk", "Disposisi 3: ${etDisposisi3.text}")
        Log.d("TambahSuratMasuk", "Tanggal Disposisi 3: ${etTanggalDisposisi3.text}")
    }
}