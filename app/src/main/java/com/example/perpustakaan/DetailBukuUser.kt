package com.example.perpustakaan

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.perpustakaan.database.PerpustakaanDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailBukuUser : AppCompatActivity() {


    private var id: Int? = null
    private lateinit var judulBuku: TextView
    private lateinit var penulisBuku: TextView
    private lateinit var tahunBuku: TextView
    private lateinit var deskripsiBuku: TextView
    private lateinit var stok: TextView
    private lateinit var imageBuku: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_buku_user)

        // Mengambil data dari Intent
        id = intent.getIntExtra("BUKU_ID", -1)

        if (id == -1) {
            Toast.makeText(this, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        judulBuku = findViewById(R.id.tv_judul_buku)
        penulisBuku = findViewById(R.id.tv_penulis)
        tahunBuku = findViewById(R.id.tv_tahun_terbit)
        stok = findViewById(R.id.tvStok)
        deskripsiBuku = findViewById(R.id.tv_deskripsi)
        imageBuku = findViewById(R.id.img_buku)

        loadBukuDetails()

    }

    private fun loadBukuDetails() {
        id?.let {
            lifecycleScope.launch(Dispatchers.IO) {

                val buku = PerpustakaanDatabase.getDatabase(this@DetailBukuUser)
                    .daobuku()
                    .getBukuById(it)

                if (buku != null) {
                    runOnUiThread {
                        // Memperbarui elemen UI dengan detail buku dari database
                        judulBuku.text = buku.judul
                        penulisBuku.text = buku.penulis
                        tahunBuku.text = buku.tahunTerbit.toString()
                        deskripsiBuku.text = buku.deskripsi
                        stok.text = buku.stok.toString()
                        imageBuku.tag = buku.gambarUrl

                        // Memuat gambar dari URL menggunakan Glide
                        Glide.with(this@DetailBukuUser)
                            .load(buku.gambarUrl)  // Menggunakan URL gambar yang diambil dari database
                            .into(imageBuku)  // Menampilkan gambar di ImageView
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@DetailBukuUser, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}