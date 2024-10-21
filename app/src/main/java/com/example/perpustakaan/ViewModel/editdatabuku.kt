package com.example.perpustakaan.ViewModel


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R
import com.example.perpustakaan.database.PerpustakaanDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class editdatabuku : AppCompatActivity() {

    private lateinit var etJudulBuku: EditText
    private lateinit var etPenulis: EditText
    private lateinit var etTahunTerbit: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var etStok: EditText
    private lateinit var btnSimpan: Button
    private lateinit var btnBatal: Button
    private var bukuId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editdatabuku)

        // Inisialisasi views
        etJudulBuku = findViewById(R.id.et_judul_buku)
        etPenulis = findViewById(R.id.et_penulis)
        etTahunTerbit = findViewById(R.id.et_tahun_terbit)
        etDeskripsi = findViewById(R.id.et_deskripsi)
        etStok = findViewById(R.id.et_stok_buku)
        btnSimpan = findViewById(R.id.btn_simpan)
        btnBatal = findViewById(R.id.btn_batal)

        // Ambil data dari Intent
        bukuId = intent.getIntExtra("BUKU_ID", -1)
        val judulBuku = intent.getStringExtra("BUKU_JUDUL")
        val penulisBuku = intent.getStringExtra("BUKU_PENULIS")
        val tahunTerbit = intent.getIntExtra("BUKU_TAHUN",0).toString()
        val deskripsiBuku = intent.getStringExtra("BUKU_DESKRIPSI")
        val stokBuku = intent.getStringExtra("BUKU_STOK")


        // Set data ke EditText
        etJudulBuku.setText(judulBuku)
        etPenulis.setText(penulisBuku)
        etTahunTerbit.setText(tahunTerbit)
        etDeskripsi.setText(deskripsiBuku)
        etStok.setText(stokBuku)


        // Simpan perubahan
        btnSimpan.setOnClickListener {
            updateDataBuku()
        }

        // Batal edit
        btnBatal.setOnClickListener {
            finish()
        }
    }

    private fun updateDataBuku() {
        val judul = etJudulBuku.text.toString()
        val penulis = etPenulis.text.toString()
        val tahun = etTahunTerbit.text.toString().toIntOrNull() ?: 0
        val deskripsi = etDeskripsi.text.toString()
        val stok = etStok.text.toString().toIntOrNull() ?: 0

        if (tahun == null) {
            runOnUiThread {
                Toast.makeText(this, "Tahun terbit tidak valid", Toast.LENGTH_SHORT).show()
            }
            return
        }

        if (bukuId != null && bukuId != -1) {
            lifecycleScope.launch(Dispatchers.IO) {
                val bukuDao = PerpustakaanDatabase.getDatabase(this@editdatabuku).daobuku()

                val buku = Buku(
                    id_buku = bukuId!!,
                    judul = judul,
                    penulis = penulis,
                    tahunTerbit = tahun,
                    deskripsi = deskripsi,
                    stok = stok
                )

                try {
                    bukuDao.update_buku(buku)
                    launch(Dispatchers.Main) {
                        Toast.makeText(this@editdatabuku, "Data buku berhasil diupdate", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(this@editdatabuku, "Gagal mengupdate data buku", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}
