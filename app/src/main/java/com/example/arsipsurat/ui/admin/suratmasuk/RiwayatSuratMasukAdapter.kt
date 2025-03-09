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
        val tvTanggalMasuk: TextView = view.findViewById(R.id.tvTanggalMasuk)
        val tvTanggalSurat: TextView = view.findViewById(R.id.tvTanggalSurat)
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

        holder.tvKodeSurat.text = "#${item.kode_surat}"
        holder.tvTanggalMasuk.text = "${DateHelper.formatDate(item.tanggal.toString())}"
        holder.tvTanggalSurat.text = "${DateHelper.formatDate(item.tanggal_surat.toString())}"
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