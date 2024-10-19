package com.example.perpustakaan.daftarbukuActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ViewModel.BukuViewModel // Import ViewModel untuk Buku
import com.example.perpustakaan.databinding.FragmentTambahdatabukuBinding

class FragmentTambahDataBuku : Fragment() {
    private var _binding: FragmentTambahdatabukuBinding? = null
    private val binding get() = _binding!!

    private lateinit var bukuViewModel: BukuViewModel // Deklarasi ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTambahdatabukuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        bukuViewModel = ViewModelProvider(this).get(BukuViewModel::class.java)

        binding.btnBatal.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnSimpanBuku.setOnClickListener {
            val judul = binding.etJudulBuku.text.toString()
            val penulis = binding.etPenulis.text.toString()
            val tahunTerbit = binding.etTahunTerbit.text.toString()
            val stok = binding.etStok.text.toString()
            val deskripsi = binding.etDeskripsi.text.toString()

            if (judul.isEmpty() || penulis.isEmpty() || tahunTerbit.isEmpty() || stok.isEmpty()) {
                Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                val tahunTerbitInt = tahunTerbit.toIntOrNull()
                val stokInt = stok.toIntOrNull()

                if (tahunTerbitInt != null && stokInt != null) {
                    val buku = Buku(
                        id_buku = 0, // ID otomatis, akan diatur oleh Room
                        judul = judul,
                        penulis = penulis,
                        tahunTerbit = tahunTerbitInt,
                        stok = stokInt,
                        deskripsi = deskripsi
                    )

                    // Menyimpan buku ke database
                    bukuViewModel.insert(buku)
                    Toast.makeText(requireContext(), "Data buku berhasil disimpan", Toast.LENGTH_SHORT).show()

                    // Kembali ke layar sebelumnya
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Tahun terbit dan stok harus berupa angka", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
