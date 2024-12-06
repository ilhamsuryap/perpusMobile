package com.example.perpustakaan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.databinding.ActivityEditdatapinjamBinding
import com.example.perpustakaan.entity.Pinjam
import com.example.perpustakaan.repository.PeminjamanRepository
import com.example.perpustakaan.ViewModel.PeminjamanViewModel

class EditDataPinjamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditdatapinjamBinding
    private lateinit var pinjamViewModel: PeminjamanViewModel
    private var pinjamId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditdatapinjamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dapatkan DAO dan buat instance repository
        val pinjamDao = PerpustakaanDatabase.getDatabase(this).daopinjam()
        val repository = PeminjamanRepository(pinjamDao)

        // Inisialisasi PeminjamanViewModel langsung tanpa ViewModelFactory
        pinjamViewModel = PeminjamanViewModel(repository)

        // Ambil data dari Intent
        pinjamId = intent.getIntExtra("id_pinjam", -1)

        // Pastikan pinjamId valid
        if (pinjamId == -1) {
            Toast.makeText(this, "ID Pinjam tidak valid", Toast.LENGTH_SHORT).show()
            finish() // Kembali ke activity sebelumnya jika ID tidak valid
            return
        }

        val namaAnggota = intent.getStringExtra("namaanggota")
        val judulBuku = intent.getStringExtra("judulbuku_pinjam")
        val tanggalPinjam = intent.getStringExtra("tanggalpinjam")
        val tanggalKembali = intent.getStringExtra("tanggalkembali")

        // Set data ke EditText
        binding.etNamaAnggota.setText(namaAnggota)
        binding.etJudulBuku.setText(judulBuku)
        binding.etTanggalPinjam.setText(tanggalPinjam)
        binding.etTanggalKembali.setText(tanggalKembali)

        // Setup Tombol Hapus
        binding.btnHapus.setOnClickListener {
            if (pinjamId != -1) {
                pinjamViewModel.deletePinjamById(pinjamId)
                Toast.makeText(this, "Buku berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke activity sebelumnya
            } else {
                Toast.makeText(this, "ID Pinjam tidak valid", Toast.LENGTH_SHORT).show()
            }
        }

        // Setup Tombol Simpan
        binding.btnSimpan.setOnClickListener {
            if (validateInputs()) {
                if (pinjamId != -1) {
                    val updatedPinjam = Pinjam(
                        pinjamId.toLong(),
                        binding.etNamaAnggota.text.toString(),
                        binding.etJudulBuku.text.toString(),
                        binding.etTanggalPinjam.text.toString(),
                        binding.etTanggalKembali.text.toString()
                    )
                    pinjamViewModel.insert(updatedPinjam)
                    Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "ID Pinjam tidak valid", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val namaAnggota = binding.etNamaAnggota.text.toString().trim()
        val judulBuku = binding.etJudulBuku.text.toString().trim()
        val tanggalPinjam = binding.etTanggalPinjam.text.toString().trim()
        val tanggalKembali = binding.etTanggalKembali.text.toString().trim()

        return when {
            namaAnggota.isEmpty() -> {
                binding.etNamaAnggota.error = "Nama anggota tidak boleh kosong"
                false
            }
            judulBuku.isEmpty() -> {
                binding.etJudulBuku.error = "Judul buku tidak boleh kosong"
                false
            }
            tanggalPinjam.isEmpty() -> {
                binding.etTanggalPinjam.error = "Tanggal pinjam tidak boleh kosong"
                false
            }
            tanggalKembali.isEmpty() -> {
                binding.etTanggalKembali.error = "Tanggal kembali tidak boleh kosong"
                false
            }
            else -> true // Semua input valid
        }
    }
}
