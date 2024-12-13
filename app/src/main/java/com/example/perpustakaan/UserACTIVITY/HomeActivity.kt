package com.example.perpustakaan.UserACTIVITY

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.perpustakaan.R
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.daftarbukuActivity.DaftarBukuActivity
import com.example.perpustakaan.pinjamActivity.PinjamBukuActivity
import com.example.perpustakaan.user.Login
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {

    private lateinit var manageLibrary: TextView
    private lateinit var addBook: TextView
    private lateinit var borrowBook: TextView
    private lateinit var menuButton: ImageView

    private val bukuViewModel: BukuViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize views
        manageLibrary = findViewById(R.id.manage_library)
        addBook = findViewById(R.id.addbook)
        borrowBook = findViewById(R.id.borrowbook)
        menuButton = findViewById(R.id.menu)

        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Atur klik listener untuk tombol menu
        menuButton.setOnClickListener {
            showPopupMenu(it)
        }

        // Listener untuk icon borrowbook
        findViewById<ImageView>(R.id.icon_borrowbook).setOnClickListener {
            startActivity(Intent(this, PinjamBukuActivity::class.java))
        }

        findViewById<ImageView>(R.id.icon_listbook).setOnClickListener {
            startActivity(Intent(this, Search::class.java))
        }

        findViewById<ImageView>(R.id.icon_openbook).setOnClickListener {
            startActivity(Intent(this, DaftarBukuActivity::class.java))
        }

        // Observe books
        observeBooks()
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

    private fun observeBooks() {
        bukuViewModel.allBuku.observe(this) { allBooks ->
            // Logika untuk memproses data buku jika diperlukan
        }
    }
}
