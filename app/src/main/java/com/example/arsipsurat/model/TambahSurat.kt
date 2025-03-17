package com.example.arsipsurat.model

data class TambahSurat(
    val kode_surat: String,
    val nomor_urut: String,
    val nomor_surat: String,
    val tanggal_masuk: String,
    val tanggal_surat: String,
    val pengirim: String,
    val id_bagian_pengirim: Int?,
    val kepada: String,
    val id_bagian_penerima: Int?,
    val perihal: String,
    val disposisi1: String?,
    val tanggal_disposisi1: String?,
    val disposisi2: String?,
    val tanggal_disposisi2: String?,
    val disposisi3: String?,
    val tanggal_disposisi3: String?
)
