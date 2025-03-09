package com.example.arsipsurat.model

data class Surat(
    val id: Int,
    val kode_surat: String?,
    val nomor_surat: String?,
    val nomor_urut: String?,
    val tanggal: String?,
    val tanggal_surat: String?,
    val penerima: String?,
    val pengirim: String?,
    val perihal: String?,
    val kategori: String?,
    val status: String?,
    val file_surat: String?,
    val id_bagian_pengirim: Int?,
    val id_bagian_penerima: Int?,
    val created_at: String?,
    val updated_at: String?,
    val disposisi_1: String?,
    val tanggal_disposisi_1: String?,
    val disposisi_2: String?,
    val tanggal_disposisi_2: String?,
    val disposisi_3: String?,
    val tanggal_disposisi_3: String?
)