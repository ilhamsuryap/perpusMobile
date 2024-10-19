package com.example.perpustakaan.daftarbukuActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perpustakaan.adapter.BukuAdapter
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.databinding.ActivityDaftarBukuBinding
import androidx.activity.viewModels

class DaftarBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBukuBinding
    private lateinit var bukuAdapter: BukuAdapter

    // Inisialisasi ViewModel
    private val bukuViewModel: BukuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        setupRecyclerView()

        // Observasi data buku dari ViewModel
        bukuViewModel.allBuku.observe(this) { bukuList ->
            bukuAdapter.setData(bukuList) // Update daftar buku di adapter saat data berubah
        }

        // Setup tombol tambah buku
        binding.btnTambahBuku.setOnClickListener {
            loadFragment(FragmentTambahDataBuku())
        }
    }

    // Setup RecyclerView dengan Adapter
    private fun setupRecyclerView() {
        bukuAdapter = BukuAdapter()
        binding.rvBuku.apply {
            layoutManager = LinearLayoutManager(this@DaftarBukuActivity)
            adapter = bukuAdapter
        }
    }

    // Fungsi untuk memuat fragment
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.root.id, fragment) // Menggunakan root sebagai container untuk fragment
        transaction.addToBackStack(null) // Optional, menambahkan ke back stack jika diperlukan
        transaction.commit()
    }
}

