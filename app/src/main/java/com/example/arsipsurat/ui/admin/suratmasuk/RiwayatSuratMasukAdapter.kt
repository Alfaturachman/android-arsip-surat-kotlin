package com.example.arsipsurat.ui.admin.suratmasuk

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.arsipsurat.R
import com.example.arsipsurat.helpers.DateHelper
import com.example.arsipsurat.model.Surat
import com.example.arsipsurat.ui.admin.suratmasuk.detail.DetailSuratMasukActivity

class RiwayatSuratMasukAdapter(
    private var agendaList: List<Surat>,
) : RecyclerView.Adapter<RiwayatSuratMasukAdapter.SuratMasukViewHolder>() {

    class SuratMasukViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKodeSurat: TextView = view.findViewById(R.id.tvKodeSurat)
        val tvNomorSurat: TextView = view.findViewById(R.id.tvNomorSurat)
        val tvTanggalMasuk: TextView = view.findViewById(R.id.tvTanggalMasuk)
        val tvTanggalSurat: TextView = view.findViewById(R.id.tvTanggalSurat)
        val tvPerihal: TextView = view.findViewById(R.id.tvPerihal)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvPenerima: TextView = view.findViewById(R.id.tvPenerima)
        val tvPengirim: TextView = view.findViewById(R.id.tvPengirim)
        val cardView: View = view.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuratMasukViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daftar_riwayat_surat_masuk, parent, false)
        return SuratMasukViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuratMasukViewHolder, position: Int) {
        val item = agendaList[position]
        val context = holder.itemView.context

        holder.tvKodeSurat.text = "Kode Surat: ${item.kode_surat}"
        holder.tvNomorSurat.text = "Nomor Surat: ${item.nomor_surat}"
        holder.tvTanggalMasuk.text = "${DateHelper.formatDate(item.tanggal.toString())}"
        holder.tvTanggalSurat.text = "${DateHelper.formatDate(item.tanggal_surat.toString())}"
        holder.tvPerihal.text = item.perihal ?: "-"
        holder.tvStatus.text = item.status ?: "-"
        holder.tvPenerima.text = item.penerima ?: "-"
        holder.tvPengirim.text = item.pengirim ?: "-"

        // Status color
        val statusColor = when (item.status) {
            "Diproses" -> R.color.badge_warning
            "Disetujui" -> R.color.badge_success
            "Ditolak" -> R.color.badge_danger
            else -> R.color.badge_secondary
        }
        holder.tvStatus.backgroundTintList = ContextCompat.getColorStateList(context, statusColor)

        // CardView click listener
        holder.cardView.setOnClickListener {
            val intent = Intent(context, DetailSuratMasukActivity::class.java).apply {
                putExtra("id_surat", item.id)
                putExtra("kode_surat", item.kode_surat)
                putExtra("nomor_surat", item.nomor_surat)
                putExtra("nomor_urut", item.nomor_urut)
                putExtra("tanggal", item.tanggal)
                putExtra("tanggal_surat", item.tanggal_surat)
                putExtra("penerima", item.penerima)
                putExtra("pengirim", item.pengirim)
                putExtra("perihal", item.perihal)
                putExtra("kategori", item.kategori)
                putExtra("status", item.status)
                putExtra("file_surat", item.file_surat)
                putExtra("id_bagian_pengirim", item.id_bagian_pengirim ?: -1)
                putExtra("id_bagian_penerima", item.id_bagian_penerima ?: -1)
                putExtra("disposisi_1", item.disposisi_1)
                putExtra("tanggal_disposisi_1", item.tanggal_disposisi_1)
                putExtra("disposisi_2", item.disposisi_2)
                putExtra("tanggal_disposisi_2", item.tanggal_disposisi_2)
                putExtra("disposisi_3", item.disposisi_3)
                putExtra("tanggal_disposisi_3", item.tanggal_disposisi_3)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = agendaList.size

    fun updateData(newData: List<Surat>) {
        agendaList = newData
        notifyDataSetChanged()
    }
}