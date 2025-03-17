package com.example.arsipsurat.model

import com.google.gson.annotations.SerializedName

data class TotalSurat (
    @SerializedName("total_surat_masuk") val totalSuratMasuk: Int,
    @SerializedName("total_surat_keluar") val totalSuratKeluar: Int
)