package com.example.arsipsurat.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.arsipsurat.databinding.FragmentDashboardBinding
import com.example.arsipsurat.ui.bagian.suratmasuk.RiwayatSuratMasukActivity
import com.example.arsipsurat.ui.bagian.suratmasuk.tambah.TambahSuratMasukActivity

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Level dari SharedPreferences
        val level = getLevelFromSharedPreferences()
        when (level) {
            "admin" -> {
                binding.layoutKategoriSuratAdmin.visibility = View.VISIBLE
                binding.layoutKategoriSuratBagian.visibility = View.GONE

                binding.cardViewSuratMasukAdmin.setOnClickListener {
                    val intent = Intent(requireContext(), com.example.arsipsurat.ui.admin.suratmasuk.RiwayatSuratMasukActivity::class.java)
                    startActivity(intent)
                }

                binding.cardViewSuratKeluarAdmin.setOnClickListener {
                    val intent = Intent(requireContext(), com.example.arsipsurat.ui.admin.suratkeluar.RiwayatSuratKeluarActivity::class.java)
                    startActivity(intent)
                }
            }
            "bagian" -> {
                binding.layoutKategoriSuratAdmin.visibility = View.GONE
                binding.layoutKategoriSuratBagian.visibility = View.VISIBLE

                binding.cardViewSuratMasukBagian.setOnClickListener {
                    val intent = Intent(requireContext(), RiwayatSuratMasukActivity::class.java)
                    startActivity(intent)
                }

                binding.cardViewSuratKeluarBagian.setOnClickListener {
                    val intent = Intent(requireContext(), TambahSuratMasukActivity::class.java)
                    startActivity(intent)
                }
            }
            else -> {
                Toast.makeText(requireContext(), "Level pengguna tidak valid", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun getLevelFromSharedPreferences(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getString("level", "0")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}