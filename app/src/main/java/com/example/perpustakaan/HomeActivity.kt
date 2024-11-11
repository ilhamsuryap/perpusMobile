package com.example.perpustakaan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView;
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ItemHome
import com.example.perpustakaan.R
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.ViewModel.ProfileActivity
import com.example.perpustakaan.adapter.BukuAdapter
import com.example.perpustakaan.daftarbukuActivity.DaftarBukuActivity
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.detailbuku.detail
import com.example.perpustakaan.pinjamActivity.PinjamBukuActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var notificationIcon: ImageView
    private lateinit var manageLibrary: TextView
    private lateinit var addBook: TextView
    private lateinit var listBooks: TextView
    private lateinit var borrowBook: TextView
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterHome
    private lateinit var database : PerpustakaanDatabase
    private var Books: MutableList<Buku> = mutableListOf() // Correct


    private val bukuViewModel: BukuViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize views
        manageLibrary = findViewById(R.id.manage_library)
        addBook = findViewById(R.id.addbook)
        borrowBook = findViewById(R.id.borrowbook)
        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerViewHome)

        // Handle notification click event
//        notificationIcon.setOnClickListener {
//            val notificationIntent = Intent(this@HomeActivity, NotificationActivity::class.java)
//            startActivity(notificationIntent)
//        }

        database = PerpustakaanDatabase.getDatabase(this)
//        adapter = AdapterHome(this, Books, database)
        setupRecyclerView()
        observeBooks()
        setupSearchObserver()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(searchText: String): Boolean {
                if (searchText.isNotEmpty()) {
                    lifecycleScope.launch {
                        bukuViewModel.cariBuku(searchText)
                    }
                } else {
                    // Jika searchText kosong, tampilkan semua buku lagi
                    observeBooks()
                }
                return true
            }
        })

        val btnTentangKami: ImageView = findViewById(R.id.tentangkami)
        btnTentangKami.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val btnBorrowBook: ImageView = findViewById(R.id.icon_borrowbook)
        btnBorrowBook.setOnClickListener(){
            val intent = Intent(this, PinjamBukuActivity::class.java)
            startActivity(intent)
        }

        val btnAddBook: ImageView = findViewById(R.id.icon_addbook)
        btnAddBook.setOnClickListener(){
            val intent = Intent(this, DaftarBukuActivity::class.java)
            startActivity(intent)
        }
    }

    // Set up the RecyclerView with the Adapter
    private fun setupRecyclerView() {
        adapter = AdapterHome(this, Books, database) { buku ->
            val intent = Intent(this, detail::class.java)
            intent.putExtra("BUKU_JUDUL", buku.judul)
            intent.putExtra("BUKU_PENULIS", buku.penulis)
            intent.putExtra("BUKU_TAHUN", buku.tahunTerbit)
            intent.putExtra("BUKU_DESKRIPSI", buku.deskripsi)
            startActivity(intent)
        } // Initialize adapter with an empty list initially
        recyclerView.layoutManager = LinearLayoutManager(this, HORIZONTAL,false,)
        recyclerView.adapter = adapter
    }

    private fun observeBooks() {
        bukuViewModel.allBuku.observe(this) { books ->
            books?.let {
                Books.clear()  // Clear the old data
                Books.addAll(books)  // Add new data from the database
                adapter.notifyDataSetChanged()  // Notify adapter about the data change
            }
        }
    }

    private fun setupSearchObserver() {
        bukuViewModel.searchResults.observe(this) { searchResults ->
            searchResults?.let {
                Books.clear()
                Books.addAll(it)
                adapter.notifyDataSetChanged()  // Update adapter dengan hasil pencarian
            }
        }
    }
}
