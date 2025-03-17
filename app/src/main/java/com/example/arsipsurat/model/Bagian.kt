package com.example.arsipsurat.model

import java.util.Date

data class Bagian(
    val id_bagian: Int,
    val nama_bagian: String,
    val username_admin_bagian: String,
    val password_bagian: String,
    val nama_lengkap: String,
    val tanggal_lahir_bagian: Date,
    val alamat: String,
    val no_hp_bagian: String,
    val gambar: String
)