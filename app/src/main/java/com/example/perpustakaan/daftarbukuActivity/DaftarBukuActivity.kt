package com.example.perpustakaan.daftarbukuActivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perpustakaan.adapter.BukuAdapter
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.databinding.ActivityDaftarBukuBinding
import androidx.activity.viewModels
import com.example.perpustakaan.R
import com.example.perpustakaan.detailbuku.detail

class DaftarBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBukuBinding
    private lateinit var bukuAdapter: BukuAdapter

    private val bukuViewModel: BukuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        bukuViewModel.allBuku.observe(this) { bukuList ->
            bukuAdapter.setData(bukuList)
        }

        binding.btnTambahBuku.setOnClickListener {
            loadFragment(FragmentTambahDataBuku())
        }
    }

    private fun setupRecyclerView() {
        bukuAdapter = BukuAdapter { buku ->
            val intent = Intent(this, detail::class.java)
            intent.putExtra("BUKU_JUDUL", buku.judul)
            intent.putExtra("BUKU_PENULIS", buku.penulis)
            intent.putExtra("BUKU_TAHUN", buku.tahunTerbit)
            intent.putExtra("BUKU_DESKRIPSI", buku.deskripsi)
            startActivity(intent)
        }

        binding.rvBuku.apply {
            layoutManager = LinearLayoutManager(this@DaftarBukuActivity)
            adapter = bukuAdapter
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.root.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
