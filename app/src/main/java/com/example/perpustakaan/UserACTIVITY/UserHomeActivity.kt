package com.example.perpustakaan.UserACTIVITY

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.DaftarBukuUser

import com.example.perpustakaan.adapter.AdapterHome
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.daftarbukuActivity.DaftarBukuActivity
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.detailbuku.DetailActivity
import com.example.perpustakaan.pinjamActivity.PinjamBukuActivity
import com.example.perpustakaan.user.Login
import com.google.firebase.auth.FirebaseAuth

class UserHomeActivity : AppCompatActivity() {

    private lateinit var manageLibrary: TextView
    private lateinit var listBook: ImageView
    private lateinit var icon_openbook : ImageView
    private lateinit var borrowBook: ImageView
    private lateinit var itex :Search
    private lateinit var searchView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterHome
    private lateinit var database: PerpustakaanDatabase
    private var books: MutableList<Buku> = mutableListOf()
    private var bookLimit6: MutableList<Buku> = books.take(5).toMutableList()

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var menuButton: ImageView

    private val bukuViewModel: BukuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user)

        // Initialize views
        manageLibrary = findViewById(R.id.manage_library)
        listBook = findViewById(R.id.icon_openbook)
        borrowBook = findViewById(R.id.icon_borrowbook)
        searchView = findViewById(R.id.editTextSearch)
        searchView.isFocusable = false
        searchView.isFocusableInTouchMode = false

        // Initialize menuButton view
        menuButton = findViewById(R.id.menuUser)

        // Inisialisasi sharedPreferences dan auth
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerViewHome)

        database = PerpustakaanDatabase.getDatabase(this)

        setupRecyclerView()
        observeBooks()  // Mengamati data buku dari ViewModel
        setupSearchView()

        // Atur klik listener untuk tombol menu
        menuButton.setOnClickListener {
            showPopupMenu(it)
        }

        borrowBook.setOnClickListener{
            val intent = Intent(this@UserHomeActivity, PinjamBukuActivity::class.java)
            startActivity(intent)
        }

        searchView.setOnClickListener{
            val intent = Intent(this@UserHomeActivity, Search::class.java)
            startActivity(intent)
        }

        listBook.setOnClickListener {
            startActivity(Intent(this@UserHomeActivity, Search::class.java))
        }
    }

    private fun showPopupMenu(anchor: View) {
        val popupMenu = PopupMenu(this, anchor)
        popupMenu.menuInflater.inflate(R.menu.menu_dropdown, popupMenu.menu)

        // Listener untuk item yang diklik di popup menu
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_tentang_kami -> {
                    startActivity(Intent(this, ProfilActivity::class.java))
                    true
                }
                R.id.menu_logout -> {
                    performLogout()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun performLogout() {
        auth.signOut()
        startActivity(Intent(this, Login::class.java))
        finish()
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

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
            val intent = Intent(this@UserHomeActivity, Search::class.java)
            startActivity(intent)
        }
    }
}