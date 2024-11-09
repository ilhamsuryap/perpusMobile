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
import com.example.perpustakaan.detailbuku.detail
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.adapter.BukuListAdapter

class DaftarBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBukuBinding
    private lateinit var bukuAdapter: BukuAdapter

    // Menggunakan ViewModel untuk mendapatkan data
    private val bukuViewModel: BukuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi RecyclerView
        setupRecyclerView()

        // Observe LiveData dari ViewModel untuk update daftar buku
        bukuViewModel.allBuku.observe(this) { bukuList ->
            bukuAdapter.setData(bukuList)
        }

        // Tombol untuk menambahkan buku baru
        binding.btnTambahBuku.setOnClickListener {
            loadFragment(FragmentTambahDataBuku())
        }
    }

    // Inisialisasi RecyclerView dan mengatur adapter
    private fun setupRecyclerView() {
        bukuAdapter = BukuAdapter(emptyList()) { buku ->
            // Pindah ke halaman detail buku
            val intent = Intent(this, detail::class.java)
            intent.putExtra("BUKU_ID", buku.id_buku)
            intent.putExtra("BUKU_JUDUL", buku.judul)
            intent.putExtra("BUKU_PENULIS", buku.penulis)
            intent.putExtra("BUKU_TAHUN", buku.tahunTerbit)
            intent.putExtra("BUKU_DESKRIPSI", buku.deskripsi)
            startActivity(intent)
        }

        // Setup RecyclerView dengan LinearLayoutManager dan adapter
        binding.rvBuku.apply {
            layoutManager = LinearLayoutManager(this@DaftarBukuActivity)
            adapter = bukuAdapter
        }
    }


// Fungsi untuk mengganti fragment, misalnya untuk menambah buku
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.root.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

//    // Fungsi untuk mengganti fragment, misalnya untuk menambah buku
//    private fun loadFragment(fragment: Fragment) {
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(binding.fragmentContainer.id, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }
//}






//package com.example.perpustakaan.daftarbukuActivity
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.perpustakaan.adapter.BukuAdapter
//import com.example.perpustakaan.ViewModel.BukuViewModel
//import com.example.perpustakaan.databinding.ActivityDaftarBukuBinding
//import androidx.activity.viewModels
//import com.example.perpustakaan.adapter.BukuListAdapter
//import com.example.perpustakaan.detailbuku.detail
//
//class DaftarBukuActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityDaftarBukuBinding
//    private lateinit var bukuAdapter: BukuAdapter
//
//    private val bukuViewModel: BukuViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDaftarBukuBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setupRecyclerView()
//
//        // Observe LiveData dari ViewModel untuk update daftar buku
//        bukuViewModel.allBuku.observe(this) { bukuList ->
//            bukuAdapter.submitList(bukuList) // Update daftar buku di adapter saat data berubah
//        }
//
//        // Tombol untuk menambahkan buku baru
//        binding.btnTambahBuku.setOnClickListener {
//            loadFragment(FragmentTambahDataBuku())
//        }
//    }
//
//    // Inisialisasi RecyclerView dan mengatur adapter
//    private fun setupRecyclerView() {
//        bukuAdapter = BukuListAdapter { buku ->
//            val intent = Intent(this, detail::class.java)
//            intent.putExtra("BUKU_ID", buku.id_buku)  // Pastikan id_buku dikirimkan ke detail
//            intent.putExtra("BUKU_JUDUL", buku.judul)
//            intent.putExtra("BUKU_PENULIS", buku.penulis)
//            intent.putExtra("BUKU_TAHUN", buku.tahunTerbit)
//            intent.putExtra("BUKU_DESKRIPSI", buku.deskripsi)
//            startActivity(intent)
//        }
//
//        // Setup RecyclerView dengan LinearLayoutManager dan adapter
//        binding.rvBuku.apply {
//            layoutManager = LinearLayoutManager(this@DaftarBukuActivity)
//            adapter = bukuAdapter
//        }
//    }
//
//    // Fungsi untuk mengganti fragment, misalnya untuk menambah buku
//    private fun loadFragment(fragment: Fragment) {
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(binding.root.id, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }
//}

