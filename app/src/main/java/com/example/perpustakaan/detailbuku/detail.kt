package com.example.perpustakaan.detailbuku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perpustakaan.R
import com.example.perpustakaan.daftarbukuActivity.EditDataBuku
import com.example.perpustakaan.database.PerpustakaanDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class detail : AppCompatActivity() {

    private lateinit var btnEdit: Button
    private lateinit var btnHapus: Button
    private var bukuId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailbuku)

        btnEdit = findViewById(R.id.btn_edit_buku)
        btnHapus = findViewById(R.id.btn_hapus_buku)

        // Mendapatkan data buku dari intent
        bukuId = intent.getIntExtra("BUKU_ID", -1)
        val judulBuku = intent.getStringExtra("BUKU_JUDUL")
        val penulisBuku = intent.getStringExtra("BUKU_PENULIS")
        val tahunTerbit = intent.getStringExtra("BUKU_TAHUN")
        val deskripsiBuku = intent.getStringExtra("BUKU_DESKRIPSI")

        // Mengisi data di tampilan
        findViewById<TextView>(R.id.tv_judul_buku).text = judulBuku
        findViewById<TextView>(R.id.tv_penulis).text = penulisBuku
        findViewById<TextView>(R.id.tv_tahun_terbit).text = tahunTerbit
        findViewById<TextView>(R.id.tv_deskripsi).text = deskripsiBuku

        // Fungsi untuk tombol edit
        btnEdit.setOnClickListener {
            val intent = Intent(this, EditDataBuku::class.java)
            startActivity(intent)
        }

        // Fungsi untuk tombol hapus
        btnHapus.setOnClickListener {
            bukuId?.let { id ->
                // Jalankan fungsi hapus di dalam Coroutine
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val bukuDao = PerpustakaanDatabase.getDatabase(this@detail).daobuku()
                        bukuDao.deleteBukuById(id)

                        // Tampilkan toast di main thread setelah penghapusan
                        launch(Dispatchers.Main) {
                            Toast.makeText(this@detail, "Buku berhasil dihapus", Toast.LENGTH_SHORT).show()
                            finish()  // Kembali ke layar sebelumnya
                        }
                    } catch (e: Exception) {
                        // Tangani error dan beri pesan toast
                        launch(Dispatchers.Main) {
                            Toast.makeText(this@detail, "Gagal menghapus Buku", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
