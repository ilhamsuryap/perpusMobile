package com.example.perpustakaan

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.daftarbukuActivity.DaftarBukuActivity
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.detailbuku.DetailActivity
import com.example.perpustakaan.pinjamActivity.PinjamBukuActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var manageLibrary: TextView
    private lateinit var addBook: TextView
    private lateinit var borrowBook: TextView
    private lateinit var adapter: AdapterHome
    private lateinit var database: PerpustakaanDatabase
    private var books: MutableList<Buku> = mutableListOf()

    private val bukuViewModel: BukuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize views
        manageLibrary = findViewById(R.id.manage_library)
        addBook = findViewById(R.id.addbook)
        borrowBook = findViewById(R.id.borrowbook)

        database = PerpustakaanDatabase.getDatabase(this)


//        findViewById<ImageView>(R.id.tentangkami).setOnClickListener {
//            startActivity(Intent(this, PinjamBukuActivity::class.java))
//        }
        findViewById<ImageView>(R.id.icon_borrowbook).setOnClickListener {
            startActivity(Intent(this, PinjamBukuActivity::class.java))
        }


        findViewById<ImageView>(R.id.icon_addbook).setOnClickListener {
            startActivity(Intent(this, DaftarBukuActivity::class.java))
        }
    }


    private fun observeBooks() {
        // Mengamati LiveData allBuku untuk mendapatkan semua buku
        bukuViewModel.allBuku.observe(this) { allBooks ->
            books.clear()
            books.addAll(allBooks)
            adapter.notifyDataSetChanged()
        }
    }
}
