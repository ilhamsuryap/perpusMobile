package com.example.perpustakaan.UserACTIVITY

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.AdapterHome
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.detailbuku.DetailActivity
import com.example.perpustakaan.pinjamActivity.PinjamBukuActivity
import com.example.perpustakaan.search

class UserHomeActivity : AppCompatActivity() {

    private lateinit var manageLibrary: TextView
    private lateinit var tentangkami: ImageView
    private lateinit var listBook: ImageView
    private lateinit var borrowBook: ImageView
    private lateinit var searchView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterHome
    private lateinit var database: PerpustakaanDatabase
    private var books: MutableList<Buku> = mutableListOf()
    private var bookLimit6: MutableList<Buku> = books.take(5).toMutableList()

    private val bukuViewModel: BukuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user)

        // Initialize views
        manageLibrary = findViewById(R.id.manage_library)
        listBook = findViewById(R.id.icon_openbook)
        borrowBook = findViewById(R.id.icon_borrowbook)
        tentangkami = findViewById(R.id.tentangkami)
        searchView = findViewById(R.id.editTextSearch)
        searchView.isFocusable = false
        searchView.isFocusableInTouchMode = false

        recyclerView = findViewById(R.id.recyclerViewHome)

        database = PerpustakaanDatabase.getDatabase(this)

        setupRecyclerView()
        observeBooks()  // Mengamati data buku dari ViewModel
        setupSearchView()

        tentangkami.setOnClickListener{
            val intent = Intent(this@UserHomeActivity, ProfilActivity::class.java)
            startActivity(intent)
        }

        borrowBook.setOnClickListener{
            val intent = Intent(this@UserHomeActivity, PinjamBukuActivity::class.java)
            startActivity(intent)
        }

        listBook.setOnClickListener{
            val intent = Intent(this@UserHomeActivity, search::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = AdapterHome(this, bookLimit6, database) { buku ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("BUKU_JUDUL", buku.judul)
            intent.putExtra("BUKU_PENULIS", buku.penulis)
            intent.putExtra("BUKU_TAHUN", buku.tahunTerbit)
            intent.putExtra("BUKU_DESKRIPSI", buku.deskripsi)
            startActivity(intent)
        }

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }

    private fun observeBooks() {
        bukuViewModel.allBuku.observe(this) { allBooks ->
            books.clear()
            books.addAll(allBooks)

            // Batasi jumlah data ke 6 buku pertama
            bookLimit6.clear()
            bookLimit6.addAll(books.take(5))

            adapter.notifyDataSetChanged() // Beritahu adapter bahwa data telah berubah
        }
    }

    private fun setupSearchView() {
        val editTextSearch: EditText = findViewById(R.id.editTextSearch)

        // Tambahkan OnClickListener ke EditText
        editTextSearch.setOnClickListener {
            val intent = Intent(this@UserHomeActivity, search::class.java)
            startActivity(intent)
        }
    }
}