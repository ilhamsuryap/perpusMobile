package com.example.perpustakaan.detailbuku

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.perpustakaan.R
import com.example.perpustakaan.daftarbukuActivity.EditDataBukuActivity
import com.example.perpustakaan.database.PerpustakaanDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class DetailActivity : AppCompatActivity() {

    private lateinit var btnEdit: Button
    private lateinit var btnHapus: Button
    private var id: Long? = null // Change to Long to match getLongExtra
    private lateinit var judulBuku: TextView
    private lateinit var penulisBuku: TextView
    private lateinit var tahunBuku: TextView
    private lateinit var deskripsiBuku: TextView
    private lateinit var imageBuku: ImageView // ImageView for book image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailbuku)

        // Retrieve data from Intent
        id = intent.getLongExtra("BUKU_ID", -1L) // Use Long for ID

        if (id == -1L) { // Compare with -1L, not null
            Toast.makeText(this, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize views
        btnEdit = findViewById(R.id.btn_edit_buku)
        btnHapus = findViewById(R.id.btn_hapus_buku)
        judulBuku = findViewById(R.id.tv_judul_buku)
        penulisBuku = findViewById(R.id.tv_penulis)
        tahunBuku = findViewById(R.id.tv_tahun_terbit)
        deskripsiBuku = findViewById(R.id.tv_deskripsi)
        imageBuku = findViewById(R.id.img_buku)

        // Load book details
        loadBukuDetails()

        btnEdit.setOnClickListener {
            val intent = Intent(this, EditDataBukuActivity::class.java).apply {
                putExtra("BUKU_ID", id)
                putExtra("JUDUL", judulBuku.text.toString())
                putExtra("PENULIS", penulisBuku.text.toString())
                putExtra("TAHUN_TERBIT", tahunBuku.text.toString())
                putExtra("DESKRIPSI", deskripsiBuku.text.toString())
                putExtra("GAMBAR_URL", imageBuku.tag?.toString()) // Ensure image URL tag is not null
            }
            startActivity(intent)
        }

        btnHapus.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                id?.let {
                    val daoBuku = PerpustakaanDatabase.getDatabase(this@DetailActivity).daobuku()

                    // Ambil URL gambar dari ImageView atau database
                    val gambarUrl = imageBuku.tag?.toString() // Gambar URL disimpan di tag ImageView

                    // Hapus buku berdasarkan ID dari database
                    daoBuku.deleteBukuById(it)

                    // Jika gambar ada dan URL-nya valid, hapus file gambar di cache
                    gambarUrl?.let { url ->
                        val file = File(url)  // Membuat File objek berdasarkan path gambar
                        if (file.exists()) {
                            val deleted = file.delete()  // Menghapus file gambar dari cache
                            if (deleted) {
                                Log.d("DetailActivity", "Gambar berhasil dihapus: $url")
                            } else {
                                Log.e("DetailActivity", "Gagal menghapus gambar: $url")
                            }
                        }
                    }

                    runOnUiThread {
                        // Tampilkan toast jika buku berhasil dihapus
                        Toast.makeText(this@DetailActivity, "Buku dan gambarnya dihapus", Toast.LENGTH_SHORT).show()
                        finish()  // Tutup activity setelah penghapusan
                    }
                }
            }
        }
    }

    private fun loadBukuDetails() {
        id?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                // Debug log to see the book ID being queried
                Log.d("DetailActivity", "Memulai query dengan ID: $it")

                val buku = PerpustakaanDatabase.getDatabase(this@DetailActivity)
                    .daobuku()
                    .getBukuById(it) // Query database based on ID

                if (buku != null) {
                    runOnUiThread {
                        // Display book details on the UI
                        judulBuku.text = buku.judul
                        penulisBuku.text = buku.penulis
                        tahunBuku.text = buku.tahunTerbit.toString()
                        deskripsiBuku.text = buku.deskripsi

                        // Save image URL in the tag of ImageView for future use
                        imageBuku.tag = buku.gambarUrl

                        // Load image using Glide
                        Glide.with(this@DetailActivity)
                            .load(buku.gambarUrl) // Image URL from database
                            .into(imageBuku)
                    }
                } else {
                    // Debug log if book is not found
                    Log.e("DetailActivity", "Buku tidak ditemukan dengan ID: $it")

                    runOnUiThread {
                        // If book not found, show a message
                        Toast.makeText(this@DetailActivity, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}
