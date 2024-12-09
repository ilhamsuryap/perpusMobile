package com.example.perpustakaan.pinjamActivity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager

import com.example.perpustakaan.ViewModel.PeminjamanViewModel

import com.example.perpustakaan.adapter.PinjamAdapter
import com.example.perpustakaan.databinding.ActivityPinjamBukuBinding

class PinjamBukuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinjamBukuBinding
    private lateinit var pinjamAdapter: PinjamAdapter

    private val pinjamViewModel: PeminjamanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinjamBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Observing data changes
        pinjamViewModel.allPinjam.observe(this) { pinjamList ->
            pinjamAdapter.setData(pinjamList) // Update daftar pinjaman di adapter saat data berubah
        }

        // Set click listener for the button
        binding.btnPinjam.setOnClickListener {
            loadFragment(FragmentFormDataPinjam())
        }
    }

    private fun setupRecyclerView() {
        pinjamAdapter = PinjamAdapter()
        binding.rvPinjam.apply {
            layoutManager = GridLayoutManager(this@PinjamBukuActivity, 2) // Grid dengan 2 kolom
            adapter = pinjamAdapter
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.root.id, fragment) // Menggunakan root sebagai container untuk fragment
        transaction.addToBackStack(null) // Optional, menambahkan ke back stack jika diperlukan
        transaction.commit()
    }
}
