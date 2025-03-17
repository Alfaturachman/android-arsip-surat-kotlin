package com.example.arsipsurat.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.arsipsurat.api.ApiResponse
import com.example.arsipsurat.api.RetrofitClient
import com.example.arsipsurat.databinding.FragmentHomeBinding
import com.example.arsipsurat.model.TotalSurat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var userNama: String = "Tidak ada"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userNama = getNamaFromSharedPreferences() ?: "Tidak ada"
        binding.tvNama.text = "Halo, $userNama"

        fetchTotalSurat()

        return root
    }

    private fun fetchTotalSurat() {
        RetrofitClient.instance.getTotalSurat().enqueue(object : Callback<ApiResponse<TotalSurat>> {
            override fun onResponse(call: Call<ApiResponse<TotalSurat>>, response: Response<ApiResponse<TotalSurat>>) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.status) {
                        apiResponse.data?.let { totalSurat ->
                            updateUI(totalSurat.totalSuratMasuk, totalSurat.totalSuratKeluar)
                        }
                    } else {
                        Toast.makeText(requireContext(), apiResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<TotalSurat>>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal mengambil data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(suratMasuk: Int, suratKeluar: Int) {
        // Set jumlah surat ke TextView
        binding.tvSuratMasuk.text = suratMasuk.toString()
        binding.tvSuratKeluar.text = suratKeluar.toString()

        // Set data ke BarChart
        val barEntries = arrayListOf<BarEntry>()
        barEntries.add(BarEntry(0f, suratMasuk.toFloat()))
        barEntries.add(BarEntry(1f, suratKeluar.toFloat()))

        val dataSet = BarDataSet(barEntries, "Jumlah Surat")
        dataSet.colors = listOf(Color.BLUE, Color.RED)

        val barData = BarData(dataSet)

        // **Set formatter agar angka di batang chart tidak memiliki desimal**
        barData.setValueFormatter(IntegerValueFormatter())

        binding.chartData.data = barData

        // Menambahkan label pada sumbu X
        val labels = listOf("Surat Masuk", "Surat Keluar")
        binding.chartData.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.chartData.xAxis.granularity = 1f
        binding.chartData.xAxis.setDrawGridLines(false)

        // Mengatur formatter untuk sumbu Y agar menampilkan nilai integer
        binding.chartData.axisLeft.valueFormatter = IntegerValueFormatter()
        binding.chartData.axisRight.valueFormatter = IntegerValueFormatter()

        // Menghilangkan grid lines pada sumbu Y
        binding.chartData.axisLeft.setDrawGridLines(false)
        binding.chartData.axisRight.setDrawGridLines(false)

        // Menambahkan deskripsi chart
        val description = Description()
        description.text = "Grafik Surat Masuk & Keluar"
        binding.chartData.description = description

        binding.chartData.invalidate() // Refresh chart
    }

    // **Formatter untuk menghilangkan angka desimal**
    class IntegerValueFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toInt().toString() // Menghilangkan desimal
        }
    }

    private fun getNamaFromSharedPreferences(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getString("nama", "0")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
