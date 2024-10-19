package com.example.perpustakaan.pinjamActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perpustakaan.ViewModel.PinjamViewModel
import com.example.perpustakaan.databinding.FragmentFormDataPinjamBinding
import com.example.perpustakaan.entity.Pinjam

class FragmentFormDataPinjam : Fragment() {
    private var _binding: FragmentFormDataPinjamBinding? = null
    private val binding get() = _binding!!

    private lateinit var pinjamViewModel: PinjamViewModel // Deklarasi ViewModel

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
        pinjamViewModel = ViewModelProvider(this).get(PinjamViewModel::class.java)

        // Setup tombol simpan
        binding.btnSimpan.setOnClickListener {
            val namaAnggota = binding.etNamaAnggota.text.toString()
            val judulBuku = binding.etJudulBuku.text.toString()
            val tanggalPinjam = binding.etTglPinjam.text.toString()
            val tanggalKembali = binding.etTglKembali.text.toString()

            // Validasi input
            if (namaAnggota.isEmpty() || judulBuku.isEmpty() || tanggalPinjam.isEmpty() || tanggalKembali.isEmpty()) {
                Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                val pinjam = Pinjam(
                    id_pinjam = 0, // ID otomatis, akan diatur oleh Room
                    namaanggota = namaAnggota,
                    judulbuku_pinjam = judulBuku,
                    tanggalpinjam = tanggalPinjam,
                    tanggalkembali = tanggalKembali,
                )

                // Menyimpan data pinjaman ke database
                pinjamViewModel.insert(pinjam)
                Toast.makeText(requireContext(), "Data pinjaman berhasil disimpan", Toast.LENGTH_SHORT).show()

                // Kembali ke layar sebelumnya
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
