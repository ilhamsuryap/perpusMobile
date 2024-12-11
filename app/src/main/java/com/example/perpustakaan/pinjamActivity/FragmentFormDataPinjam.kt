package com.example.perpustakaan.pinjamActivity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.ViewModel.PeminjamanViewModel
import com.example.perpustakaan.databinding.FragmentFormDataPinjamBinding
import com.example.perpustakaan.entity.Pinjam
import java.util.Calendar

class FragmentFormDataPinjam : Fragment() {
    private var _binding: FragmentFormDataPinjamBinding? = null
    private val binding get() = _binding!!


    private lateinit var pinjamViewModel: PeminjamanViewModel // Deklarasi ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormDataPinjamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        pinjamViewModel = ViewModelProvider(this).get(PeminjamanViewModel::class.java)

        // Setup DatePicker untuk etTglPinjam
        binding.etTglPinjam.setOnClickListener {
            showDatePicker { date ->
                binding.etTglPinjam.setText(date)
            }
        }

        // Setup DatePicker untuk etTglKembali
        binding.etTglKembali.setOnClickListener {
            showDatePicker { date ->
                binding.etTglKembali.setText(date)
            }
        }

        // Setup tombol simpan
        binding.btnSimpan.setOnClickListener {
            val namaAnggota = binding.etNamaAnggota.text.toString()
            val judulBuku = binding.etJudulBuku.text.toString()
            val tanggalPinjam = binding.etTglPinjam.text.toString()
            val tanggalKembali = binding.etTglKembali.text.toString()


            // Validasi input untuk memastikan tidak ada yang kosong
            if (namaAnggota.isEmpty() || judulBuku.isEmpty() || tanggalPinjam.isEmpty() || tanggalKembali.isEmpty()) {
                Toast.makeText(requireContext(), "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            try {
                val pinjam = Pinjam(
                    namaanggota = namaAnggota,
                    judulbuku_pinjam = judulBuku,
                    tanggalpinjam = tanggalPinjam,
                    tanggalkembali = tanggalKembali,
                )


                // Menyimpan buku menggunakan ViewModel
                pinjamViewModel.insert(pinjam)
                Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), PinjamBukuActivity::class.java)
                startActivity(intent)




            } catch (e: NumberFormatException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Terjadi kesalahan saat menyimpan data", Toast.LENGTH_SHORT).show()

            }
        }
    }



    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        Toast.makeText(requireContext(), "Memunculkan DatePicker", Toast.LENGTH_SHORT).show()

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(date)
        }, year, month, day)

        datePickerDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
