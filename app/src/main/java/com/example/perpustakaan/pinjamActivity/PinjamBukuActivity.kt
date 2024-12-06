package com.example.perpustakaan.pinjamActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perpustakaan.R
import com.example.perpustakaan.ViewModel.PeminjamanViewModel
import com.example.perpustakaan.adapter.PinjamListAdapter
import com.example.perpustakaan.databinding.ActivityPinjamBukuBinding
import com.example.perpustakaan.entity.Pinjam
import com.example.perpustakaan.fragment_dikembalikan

class PinjamBukuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinjamBukuBinding
    private lateinit var pinjamAdapter: PinjamListAdapter

    private val pinjamViewModel: PeminjamanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinjamBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Observing data changes
        pinjamViewModel.peminjamanList.observe(this) { pinjamList ->
            pinjamAdapter.submitList(pinjamList) // Gunakan submitList untuk memperbarui data di adapter
        }

        // Set click listener for the button
        binding.btnPinjam.setOnClickListener {
            loadFragment(FragmentFormDataPinjam())
        }
    }

    private fun setupRecyclerView() {
        pinjamAdapter = PinjamListAdapter { pinjam ->
            // Handling item click here, if needed
            onItemClicked(pinjam)
        }
        binding.rvPinjam.apply {
            layoutManager = GridLayoutManager(this@PinjamBukuActivity, 2)
            adapter = pinjamAdapter
        }
    }

    private fun onItemClicked(pinjam: Pinjam) {
        val fragment = fragment_dikembalikan.newInstance(
            param1 = pinjam.id_pinjam.toString(),
            param2 = pinjam.namaanggota
        )
        loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment) // Pastikan `R.id.fragment_container` adalah ID container yang valid
        transaction.addToBackStack(null) // Tambahkan ke back stack untuk navigasi kembali
        transaction.commit()
    }
}
