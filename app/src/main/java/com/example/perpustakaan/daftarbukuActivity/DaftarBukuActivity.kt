
package com.example.perpustakaan.daftarbukuActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.perpustakaan.adapter.BukuAdapter
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.databinding.ActivityDaftarBukuBinding
import androidx.activity.viewModels
import com.example.perpustakaan.detailbuku.detail
import com.example.perpustakaan.Dao.Buku

class DaftarBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBukuBinding
    private lateinit var bukuAdapter: BukuAdapter

    // ViewModel to get data
    private val bukuViewModel: BukuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        setupRecyclerView()

        // Observe LiveData from ViewModel to update book list
        bukuViewModel.allBuku.observe(this) { bukuList ->
            bukuAdapter.setData(bukuList)
        }

        // Button to add a new book
        binding.btnTambahBuku.setOnClickListener {
            loadFragment(FragmentTambahDataBuku())
        }
    }

    // Initialize RecyclerView and set up adapter
    private fun setupRecyclerView() {
        bukuAdapter = BukuAdapter { buku ->
            // Navigate to book detail page
            val intent = Intent(this, detail::class.java).apply {
                putExtra("BUKU_ID", buku.id_buku)
                putExtra("BUKU_JUDUL", buku.judul)
                putExtra("BUKU_PENULIS", buku.penulis)
                putExtra("BUKU_TAHUN", buku.tahunTerbit)
                putExtra("BUKU_DESKRIPSI", buku.deskripsi)
            }
            startActivity(intent)
        }

        // Setup RecyclerView with GridLayoutManager and adapter
        binding.rvBuku.apply {
            layoutManager = GridLayoutManager(this@DaftarBukuActivity, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        // Check book stock, if 0 then span size 2, otherwise span size 1
                        return if (bukuAdapter.currentList[position].stok == 0) 2 else 1
                    }
                }
            }
            adapter = bukuAdapter
        }
    }

    // Function to replace fragment, e.g., to add a book
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.root.id, fragment)
            .addToBackStack(null)
            .commit()
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

