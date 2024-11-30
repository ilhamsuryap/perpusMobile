package com.example.perpustakaan.pinjamActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.perpustakaan.ViewModel.PeminjamanViewModel
import com.example.perpustakaan.adapter.PinjamListAdapter
import com.example.perpustakaan.databinding.ActivityPinjamBukuBinding
import com.example.perpustakaan.EditDataPinjamActivity
import com.example.perpustakaan.entity.Pinjam

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

//    private fun setupRecyclerView() {
//        pinjamAdapter = PinjamListAdapter { pinjam ->
//            // Handling item click here, if needed
//            onItemClicked(pinjam)
private fun setupRecyclerView() {
    pinjamAdapter = PinjamListAdapter { pinjam ->
        // Handling item click here, if needed
        onItemClicked(pinjam)
        }
        binding.rvPinjam.apply {
            layoutManager = GridLayoutManager(this@PinjamBukuActivity,2)
            adapter = pinjamAdapter
        }
    }

    private fun onItemClicked(pinjam: Pinjam) {
        val intent = Intent(this, EditDataPinjamActivity::class.java).apply {
            putExtra("id", pinjam.id)
            putExtra("namaanggota", pinjam.namaUser)
            putExtra("judulbuku_pinjam", pinjam.judulBuku)
            putExtra("tanggalpinjam", pinjam.tanggalPinjam)
            putExtra("tanggalkembali", pinjam.tanggalKembali)
        }
        startActivity(intent) // Membuka aktivitas editdatapinjam dengan data yang diklik
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.root.id, fragment) // Menggunakan root sebagai container untuk fragment
        transaction.addToBackStack(null) // Optional, menambahkan ke back stack jika diperlukan
        transaction.commit()
    }
}
