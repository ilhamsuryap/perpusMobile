package com.example.perpustakaan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perpustakaan.ViewModel.PeminjamanViewModel
import com.example.perpustakaan.databinding.FragmentFormDataPinjamBinding
import com.example.perpustakaan.entity.Pinjam
class FragmentFormDataPinjam : Fragment() {

    private var _binding: FragmentFormDataPinjamBinding? = null
    private val binding get() = _binding!!

    private lateinit var pinjamViewModel: PeminjamanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormDataPinjamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        pinjamViewModel = ViewModelProvider(this).get(PeminjamanViewModel::class.java)

        // Klik tombol simpan
        binding.btnSimpan.setOnClickListener {
            simpanData()
        }
    }

    private fun simpanData() {
        // Ambil input dari EditText
        val intent = Intent(context, AdminPengembalian::class.java)
        val namaAnggota = binding.etNamaAnggota.text.toString()
        val judulBuku = binding.etJudulBuku.text.toString()
        val tanggalPinjam = binding.etTglPinjam.text.toString()
        val tanggalKembali = binding.etTglKembali.text.toString()
        startActivity(intent)


        // Validasi input
        if (namaAnggota.isEmpty() || judulBuku.isEmpty() || tanggalPinjam.isEmpty() || tanggalKembali.isEmpty()) {
            Toast.makeText(requireContext(), "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        // Buat objek Pinjam baru
        val pinjam = Pinjam(
            namaanggota = namaAnggota,
            judulbuku_pinjam = judulBuku,
            tanggalpinjam = tanggalPinjam,
            tanggalkembali = tanggalKembali,
            status = "dipinjam" // Set status default
        )

        // Simpan ke database
        pinjamViewModel.insert(pinjam)

        // Tampilkan notifikasi
        Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show()

        // Kembali ke fragment sebelumnya
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}