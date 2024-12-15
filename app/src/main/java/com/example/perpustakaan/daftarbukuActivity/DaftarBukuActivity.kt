package com.example.perpustakaan.daftarbukuActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.perpustakaan.adapter.BukuAdapter
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.databinding.ActivityDaftarBukuBinding
import androidx.activity.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R
import com.example.perpustakaan.detailbuku.DetailActivity

class DaftarBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBukuBinding
    private lateinit var bukuAdapter: BukuAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val bukuViewModel: BukuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Observasi data dari Room
        bukuViewModel.allBuku.observe(this) { bukuList ->
            updateRecyclerView(bukuList)
        }

        // Tombol untuk menambah buku baru
        binding.btnTambahBuku.setOnClickListener {
            loadFragment(FragmentTambahDataBuku())
        }

        // Setup SwipeRefreshLayout
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun setupRecyclerView() {
        bukuAdapter = BukuAdapter { buku ->
            // Aksi saat item di RecyclerView diklik, buka detail buku
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id", buku.id) // Menyertakan id buku di intent
            startActivity(intent)
        }

        binding.rvBuku.apply {
            layoutManager = GridLayoutManager(this@DaftarBukuActivity, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val item = bukuAdapter.currentList.getOrNull(position)
                        return if (item?.stok == 0) 2 else 1 // Jika stok 0, gunakan 2 kolom
                    }
                }
            }
            adapter = bukuAdapter
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.daftarbuku, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun refreshData() {
        bukuViewModel.syncBuku {
            // Menyembunyikan indikator refresh setelah data diperbarui
            swipeRefreshLayout.isRefreshing = false
            Toast.makeText(this, "Data diperbarui", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateRecyclerView(bukuList: List<Buku>) {
        bukuAdapter.submitList(bukuList)
    }
}
