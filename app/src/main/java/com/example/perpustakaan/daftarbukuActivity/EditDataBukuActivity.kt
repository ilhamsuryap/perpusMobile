package com.example.perpustakaan.daftarbukuActivity

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.databinding.ActivityEditdatabukuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditDataBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditdatabukuBinding
    private var bukuId: Long? = null // Change bukuId type to Long
    private var imageUri: String? = null  // Variabel untuk menyimpan image URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditdatabukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        bukuId = intent.getLongExtra("BUKU_ID", -1L) // Ensure bukuId is retrieved as Long
        val judulBuku = intent.getStringExtra("BUKU_JUDUL")
        val penulisBuku = intent.getStringExtra("BUKU_PENULIS")
        val tahunTerbit = intent.getIntExtra("BUKU_TAHUN", 0).toString()
        val deskripsiBuku = intent.getStringExtra("BUKU_DESKRIPSI")
        val stokBuku = intent.getIntExtra("BUKU_STOK", 0).toString()
        val imageUrl = intent.getStringExtra("BUKU_IMAGE_URL")

        // Set data ke EditText
        binding.etJudulBuku.setText(judulBuku)
        binding.etPenulis.setText(penulisBuku)
        binding.etTahunTerbit.setText(tahunTerbit)
        binding.etDeskripsi.setText(deskripsiBuku)
        binding.etStokBuku.setText(stokBuku)

        // Jika ada image URL, load dengan Glide
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(binding.ivBuku)
        }

        // Pilih gambar
        binding.btnSelectImage.setOnClickListener {
            openGallery() // Membuka galeri untuk memilih gambar baru
        }

        // Simpan perubahan
        binding.btnSimpan.setOnClickListener {
            updateDataBuku() // Update data buku, termasuk gambar
        }

        // Batal edit
        binding.btnBatal.setOnClickListener {
            finish()
        }
    }

    private fun openGallery() {
        // Membuka galeri gambar menggunakan Intent
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Dapatkan URI gambar dari galeri
            val selectedImageUri = data?.data
            selectedImageUri?.let {
                imageUri = it.toString()  // Menyimpan URI gambar dalam string
                // Tampilkan gambar yang dipilih menggunakan Glide
                Glide.with(this)
                    .load(it)
                    .into(binding.ivBuku)
            }
        }
    }

    private fun updateDataBuku() {
        val judul = binding.etJudulBuku.text.toString()
        val penulis = binding.etPenulis.text.toString()
        val tahun = binding.etTahunTerbit.text.toString().toIntOrNull() ?: 0
        val deskripsi = binding.etDeskripsi.text.toString()
        val stok = binding.etStokBuku.text.toString().toIntOrNull() ?: 0

        // Validasi input
        if (judul.isEmpty() || penulis.isEmpty() || deskripsi.isEmpty() || tahun == 0 || stok == 0) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Pastikan bukuId valid
        val validBukuId = bukuId ?: run {
            Toast.makeText(this, "ID Buku tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        // Lakukan update jika ID valid
        lifecycleScope.launch(Dispatchers.IO) {
            val bukuDao = PerpustakaanDatabase.getDatabase(this@EditDataBukuActivity).daobuku()

            // Ambil imageUri jika ada
            val imageUriString = imageUri ?: ""

            // Buat objek Buku dengan data baru
            val buku = Buku(
                id = validBukuId,  // Menggunakan bukuId yang valid (Long)
                judul = judul,
                penulis = penulis,
                tahunTerbit = tahun,
                deskripsi = deskripsi,
                stok = stok,
                gambarUrl = imageUriString // Menyimpan URL gambar jika ada
            )

            try {
                // Update data buku di database
                bukuDao.update(buku)
                launch(Dispatchers.Main) {
                    Toast.makeText(this@EditDataBukuActivity, "Data buku berhasil diupdate", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke activity sebelumnya setelah berhasil
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Toast.makeText(this@EditDataBukuActivity, "Gagal mengupdate data buku", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 1001
    }
}
