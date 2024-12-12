
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
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R
import com.example.perpustakaan.detailbuku.DetailActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DaftarBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBukuBinding
    private lateinit var bukuAdapter: BukuAdapter

    private val bukuViewModel: BukuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        loadFragment(FragmentTambahDataBuku())

        setupRecyclerView()
        syncToFirebase()


        // Observe LiveData dari ViewModel
        bukuViewModel.allBuku.observe(this) { bukuList ->
            bukuAdapter.submitList(bukuList)
        }

        // Tombol untuk menambah buku baru
        binding.btnTambahBuku.setOnClickListener {
            loadFragment(FragmentTambahDataBuku())
        }
    }
    private fun syncToFirebase() {
        val firebaseRef = FirebaseDatabase.getInstance().getReference("buku")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bukuList = mutableListOf<Buku>()

                for (dataSnapshot in snapshot.children) {
                    val buku = dataSnapshot.getValue(Buku::class.java)
                    if (buku != null) {
                        bukuList.add(buku)
                    }
                }

                // Perbarui adapter dengan daftar buku
                bukuAdapter.submitList(bukuList)
                bukuViewModel.syncLocalDatabase(bukuList)
                bukuViewModel.syncUnsyncedData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DaftarBukuActivity, "Gagal mengambil data dari Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {
        bukuAdapter = BukuAdapter { buku ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("BUKU_ID", buku.id)  // Pastikan id_buku dikirimkan ke detail
            intent.putExtra("BUKU_JUDUL", buku.judul)
            intent.putExtra("BUKU_PENULIS", buku.penulis)
            intent.putExtra("BUKU_TAHUN", buku.tahunTerbit)
            intent.putExtra("BUKU_DESKRIPSI", buku.deskripsi)
            intent.putExtra("BUKU_STOK", buku.stok)
            intent.putExtra("BUKU_IMAGE_URL", buku.gambarUrl)
            startActivity(intent)
        }
        binding.rvBuku.apply {
            layoutManager = GridLayoutManager(this@DaftarBukuActivity, 2).apply {
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

