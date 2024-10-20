package com.example.perpustakaan

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.perpustakaan.R
import com.example.perpustakaan.ViewModel.PinjamViewModel
import com.example.perpustakaan.databinding.ActivityEditdatapinjamBinding
import com.example.perpustakaan.entity.Pinjam


class editdatapinjam : AppCompatActivity() {
    private lateinit var binding: ActivityEditdatapinjamBinding
    private val pinjamViewModel: PinjamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditdatapinjamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data from Intent
        val idPinjam = intent.getIntExtra("id_pinjam", -1) // Ambil id_pinjam
        val namaAnggota = intent.getStringExtra("namaanggota")
        val judulBuku = intent.getStringExtra("judulbuku_pinjam")
        val tanggalPinjam = intent.getStringExtra("tanggalpinjam")
        val tanggalKembali = intent.getStringExtra("tanggalkembali")

        // Populate the EditText fields
        binding.etNamaAnggota.setText(namaAnggota)
        binding.etJudulBuku.setText(judulBuku)
        binding.etTanggalPinjam.setText(tanggalPinjam)
        binding.etTanggalKembali.setText(tanggalKembali)

        // Set up the save button click listener
        binding.btnSimpan.setOnClickListener {
            if (validateInputs()) {
                Log.d("EditDataPinjam", "Update button clicked, preparing to update")
                if (idPinjam != -1) {
                    // Ambil data dari EditText
                    val updatedNamaAnggota = binding.etNamaAnggota.text.toString()
                    val updatedJudulBuku = binding.etJudulBuku.text.toString()
                    val updatedTanggalPinjam = binding.etTanggalPinjam.text.toString()
                    val updatedTanggalKembali = binding.etTanggalKembali.text.toString()

                    // Buat objek Pinjam dengan data baru
                    val updatedPinjam = Pinjam(idPinjam, updatedNamaAnggota, updatedJudulBuku, updatedTanggalPinjam, updatedTanggalKembali)

                    // Panggil metode di ViewModel untuk mengupdate data
                    pinjamViewModel.update(updatedPinjam)

                    Log.d("EditDataPinjam", "Data berhasil diperbarui: $updatedPinjam")
                    Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke aktivitas sebelumnya
                } else {
                    Log.e("EditDataPinjam", "ID Pinjam tidak valid")
                    Toast.makeText(this, "ID Pinjam tidak valid", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
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