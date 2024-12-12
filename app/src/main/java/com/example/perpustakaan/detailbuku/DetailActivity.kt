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
    private var id: Int? = null // Mengubah id menjadi Int
    private lateinit var judulBuku: TextView
    private lateinit var penulisBuku: TextView
    private lateinit var tahunBuku: TextView
    private lateinit var deskripsiBuku: TextView
    private lateinit var stok: TextView
    private lateinit var imageBuku: ImageView // ImageView untuk gambar buku

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailbuku)

        // Mengambil data dari Intent
        id = intent.getIntExtra("BUKU_ID", -1) // Menggunakan Int untuk ID (default -1 untuk ID tidak valid)

        if (id == -1) { // Memeriksa apakah ID valid
            Toast.makeText(this, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish() // Menutup activity jika buku tidak ditemukan
            return
        }

        // Inisialisasi tampilan
        btnEdit = findViewById(R.id.btn_edit_buku)
        btnHapus = findViewById(R.id.btn_hapus_buku)
        judulBuku = findViewById(R.id.tv_judul_buku)
        penulisBuku = findViewById(R.id.tv_penulis)
        tahunBuku = findViewById(R.id.tv_tahun_terbit)
        stok = findViewById(R.id.tvStok)
        deskripsiBuku = findViewById(R.id.tv_deskripsi)
        imageBuku = findViewById(R.id.img_buku)

        // Memuat detail buku
        loadBukuDetails()

        // Listener tombol Edit
        btnEdit.setOnClickListener {
            val intent = Intent(this, EditDataBukuActivity::class.java).apply {
                putExtra("BUKU_ID", id)  // Mengirimkan ID buku
                putExtra("JUDUL", judulBuku.text.toString())  // Mengirimkan detail buku
                putExtra("PENULIS", penulisBuku.text.toString())
                putExtra("TAHUN_TERBIT", tahunBuku.text.toString())
                putExtra("STOK", stok.text.toString())
                putExtra("DESKRIPSI", deskripsiBuku.text.toString())
                putExtra("GAMBAR_URL", imageBuku.tag?.toString())  // Mengirimkan URL gambar dari tag ImageView
            }
            startActivity(intent)
        }

        // Listener tombol Hapus
        btnHapus.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                id?.let {
                    val daoBuku = PerpustakaanDatabase.getDatabase(this@DetailActivity).daobuku()

                    // Mengambil URL gambar dari tag ImageView (mungkin disimpan dalam tag ImageView)
                    val gambarUrl = imageBuku.tag?.toString()

                    // Menghapus buku dari database berdasarkan ID
                    daoBuku.deleteBukuById(it)

                    // Jika ada URL gambar, mencoba menghapus file gambar dari penyimpanan lokal
                    gambarUrl?.let { url ->
                        val file = File(url) // Membuat objek File berdasarkan path URL gambar
                        if (file.exists()) {
                            val deleted = file.delete()  // Mencoba menghapus file gambar dari cache
                            if (deleted) {
                                Log.d("DetailActivity", "Gambar berhasil dihapus: $url")
                            } else {
                                Log.e("DetailActivity", "Gagal menghapus gambar: $url")
                            }
                        }
                    }

                    runOnUiThread {
                        Toast.makeText(this@DetailActivity, "Buku dan gambarnya dihapus", Toast.LENGTH_SHORT).show()
                        finish()  // Menutup activity setelah penghapusan
                    }
                }
            }
        }
    }

    private fun loadBukuDetails() {
        id?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                // Log untuk memeriksa apakah ID buku yang benar sedang diproses
                Log.d("DetailActivity", "Memulai query dengan ID: $it")

                val buku = PerpustakaanDatabase.getDatabase(this@DetailActivity)
                    .daobuku()
                    .getBukuById(it)  // Mengambil data buku berdasarkan ID

                if (buku != null) {
                    runOnUiThread {
                        // Memperbarui elemen UI dengan detail buku dari database
                        judulBuku.text = buku.judul
                        penulisBuku.text = buku.penulis
                        tahunBuku.text = buku.tahunTerbit.toString()
                        deskripsiBuku.text = buku.deskripsi
                        stok.text = buku.stok.toString()
                        // Menyimpan URL gambar di tag ImageView
                        imageBuku.tag = buku.gambarUrl

                        // Memuat gambar dari URL menggunakan Glide
                        Glide.with(this@DetailActivity)
                            .load(buku.gambarUrl)  // Menggunakan URL gambar yang diambil dari database
                            .into(imageBuku)  // Menampilkan gambar di ImageView
                    }
                } else {
                    // Log jika buku tidak ditemukan
                    Log.e("DetailActivity", "Buku tidak ditemukan dengan ID: $it")

                    runOnUiThread {
                        // Jika buku tidak ditemukan, tampilkan pesan
                        Toast.makeText(this@DetailActivity, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
                        finish()  // Menutup activity jika buku tidak ditemukan
                    }
                }
            }
        }
    }
}
