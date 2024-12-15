package com.example.perpustakaan

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
import com.example.perpustakaan.daftarbukuActivity.FragmentTambahDataBuku
import com.example.perpustakaan.databinding.ActivityDaftarBukuUserBinding
import com.example.perpustakaan.detailbuku.DetailActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DaftarBukuUser : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBukuUserBinding
    private lateinit var bukuAdapter: BukuAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    private val bukuViewModel: BukuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBukuUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
            loadFragment(FragmentTambahDataBuku())

        setupRecyclerView()


        // Observe LiveData dari ViewModel
        bukuViewModel.allBuku.observe(this) { bukuList ->
            bukuAdapter.submitList(bukuList)
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }


    private fun setupRecyclerView() {
        bukuAdapter = BukuAdapter { buku ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("BUKU_ID", buku.id)
            intent.putExtra("BUKU_JUDUL", buku.judul)
            intent.putExtra("BUKU_PENULIS", buku.penulis)
            intent.putExtra("BUKU_TAHUN", buku.tahunTerbit)
            intent.putExtra("BUKU_DESKRIPSI", buku.deskripsi)
            intent.putExtra("BUKU_STOK", buku.stok)
            intent.putExtra("BUKU_IMAGE_URL", buku.gambarUrl)
            startActivity(intent)
        }
        binding.rvBuku.apply {
            layoutManager = GridLayoutManager(this@DaftarBukuUser, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val item = bukuAdapter.currentList.getOrNull(position)
                        return if (item?.stok == 0) 2 else 1
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
        swipeRefreshLayout.isRefreshing = false
    }
}
