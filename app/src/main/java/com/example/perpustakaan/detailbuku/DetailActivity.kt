package com.example.perpustakaan.detailbuku

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private var id: Int? = null
    private lateinit var judulBuku: TextView
    private lateinit var penulisBuku: TextView
    private lateinit var tahunBuku: TextView
    private lateinit var deskripsiBuku: TextView
    private lateinit var stok: TextView
    private lateinit var imageBuku: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailbuku)

        id = intent.getIntExtra("BUKU_ID", -1)

        if (id == -1) {
            Toast.makeText(this, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val isAdmin = checkIfUserIsAdmin()

        if (!isAdmin) {
            findViewById<Button>(R.id.btn_edit_buku).visibility = View.GONE
            findViewById<Button>(R.id.btn_hapus_buku).visibility = View.GONE
        }

        btnEdit = findViewById(R.id.btn_edit_buku)
        btnHapus = findViewById(R.id.btn_hapus_buku)
        judulBuku = findViewById(R.id.tv_judul_buku)
        penulisBuku = findViewById(R.id.tv_penulis)
        tahunBuku = findViewById(R.id.tv_tahun_terbit)
        stok = findViewById(R.id.tvStok)
        deskripsiBuku = findViewById(R.id.tv_deskripsi)
        imageBuku = findViewById(R.id.img_buku)

        loadBukuDetails()

        btnEdit.setOnClickListener {
            val intent = Intent(this, EditDataBukuActivity::class.java).apply {
                putExtra("BUKU_ID", id)
                putExtra("JUDUL", judulBuku.text.toString())
                putExtra("PENULIS", penulisBuku.text.toString())
                putExtra("TAHUN_TERBIT", tahunBuku.text.toString())
                putExtra("STOK", stok.text.toString())
                putExtra("DESKRIPSI", deskripsiBuku.text.toString())
                putExtra("GAMBAR_URL", imageBuku.tag?.toString())
            }
            startActivity(intent)
        }

        btnHapus.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                id?.let {
                    val daoBuku = PerpustakaanDatabase.getDatabase(this@DetailActivity).daobuku()

                    val gambarUrl = imageBuku.tag?.toString()

                    daoBuku.deleteBukuById(it)

                    gambarUrl?.let { url ->
                        val file = File(url)
                        if (file.exists()) {
                            val deleted = file.delete()
                            if (deleted) {
                                Log.d("DetailActivity", "Gambar berhasil dihapus: $url")
                            } else {
                                Log.e("DetailActivity", "Gagal menghapus gambar: $url")
                            }
                        }
                    }

                    runOnUiThread {
                        Toast.makeText(this@DetailActivity, "Buku dan gambarnya dihapus", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun checkIfUserIsAdmin(): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val role = sharedPreferences.getString("USER_ROLE", "USER")
        return role == "ADMIN"
    }

    private fun loadBukuDetails() {
        id?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                val buku = PerpustakaanDatabase.getDatabase(this@DetailActivity)
                    .daobuku()
                    .getBukuById(it)

                if (buku != null) {
                    runOnUiThread {
                        // Update UI jika buku ditemukan
                        judulBuku.text = buku.judul
                        penulisBuku.text = buku.penulis
                        tahunBuku.text = buku.tahunTerbit.toString()
                        deskripsiBuku.text = buku.deskripsi
                        stok.text = buku.stok.toString()
                        imageBuku.tag = buku.gambarUrl

                        Glide.with(this@DetailActivity)
                            .load(buku.gambarUrl)
                            .into(imageBuku)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@DetailActivity, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}
