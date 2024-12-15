package com.example.perpustakaan.daftarbukuActivity

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.adapter.BukuAdapter
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.databinding.ActivityEditdatabukuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditDataBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditdatabukuBinding
    private var bukuId: Int? = null // Ubah tipe bukuId menjadi Int
    private var imageUri: String? = null  // Variabel untuk menyimpan image URI
    private val bukuViewModel: BukuViewModel by viewModels()
    private lateinit var bukuAdapter:BukuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditdatabukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        bukuId = intent.getIntExtra("BUKU_ID", -1) // Pastikan bukuId diterima sebagai Int
        val judulBuku = intent.getStringExtra("JUDUL")
        val penulisBuku = intent.getStringExtra("PENULIS")

// Pastikan tahunTerbit tidak 0, jika 0 ganti dengan nilai default
        val tahunTerbit = intent.getIntExtra("TAHUN_TERBIT", -1).takeIf { it != -1 }?.toString() ?: "Tahun tidak tersedia"

// Pastikan stokBuku tidak 0, jika 0 ganti dengan nilai default
        val stokBuku = intent.getIntExtra("STOK", -1).takeIf { it != -1 }?.toString() ?: "Stok tidak tersedia"

        val deskripsiBuku = intent.getStringExtra("DESKRIPSI")
        val imageUrl = intent.getStringExtra("GAMBAR_URL")


        // Set data ke EditText
        binding.etJudulBuku.setText(judulBuku)
        binding.etPenulis.setText(penulisBuku)
        binding.etTahunTerbit.setText(tahunTerbit)
        binding.etDeskripsi.setText(deskripsiBuku)
        binding.etStokBuku.setText(stokBuku)
        binding.ivBuku.setImageResource(android.R.color.transparent) // Set gambar default
        imageUri = imageUrl // Set URI gambar

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
            editBuku() // Update data buku, termasuk gambar
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

    //    private fun updateDataBuku() {
//        val selectedBuku = bukuAdapter.SelectedBuku()
//        if (selectedBuku != null) {
//            AlertDialog.Builder(this).apply {
//                setTitle("Hapus Buku")
//                setMessage("yakin ingin menghapus buku ini?")
//                setPositiveButton("Ya") { _, _ ->
//                    bukuViewModel.delete(selectedBuku)
//                    clearForm() // Memanggil fungsi untuk membersihkan form
//                }
//                setNegativeButton("Batal", null)
//            }.show()
//        } else {
//            Toast.makeText(this, "Pilih buku yang ingin dihapus", Toast.LENGTH_SHORT).show()
//        }
//    }
    private fun editBuku() {
        val judulBuku = binding.etJudulBuku.text.toString()
        val penulisBuku = binding.etPenulis.text.toString()
        val tahunTerbit = binding.etTahunTerbit.text.toString()
        val deskripsiBuku = binding.etDeskripsi.text.toString()
        val stokBuku = binding.etStokBuku.text.toString()

        if (judulBuku.isNotEmpty() && penulisBuku.isNotEmpty() && tahunTerbit.isNotEmpty() &&
            deskripsiBuku.isNotEmpty() && stokBuku.isNotEmpty()
        ) {
            // Mendapatkan URI gambar yang diperbarui
            val imageUrl = imageUri ?: "" // Gunakan URL gambar baru atau kosongkan jika tidak ada

            // Membuat objek Buku baru dengan data yang diperbarui
            val updatedBuku = Buku(
                id = bukuId ?: -1,  // Gunakan bukuId yang diterima dari intent
                judul = judulBuku,
                penulis = penulisBuku,
                tahunTerbit = tahunTerbit.toInt(),
                deskripsi = deskripsiBuku,
                stok = stokBuku.toInt(),
                gambarUrl = imageUrl
            )

            // Memanggil fungsi update di ViewModel untuk memperbarui data di Firebase dan Room
            bukuViewModel.updateBuku(updatedBuku)

            // Menampilkan pesan berhasil
            Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()

            // Menutup Activity setelah update
            finish()

        } else {
            Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 1001
    }
}
