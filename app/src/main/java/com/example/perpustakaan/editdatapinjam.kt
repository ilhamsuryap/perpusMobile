package com.example.perpustakaan

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perpustakaan.databinding.ActivityEditdatapinjamBinding
import com.example.perpustakaan.entity.Pinjam
import com.example.perpustakaan.ViewModel.PinjamViewModel
import com.example.perpustakaan.database.PerpustakaanDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class editdatapinjam : AppCompatActivity() {
    private lateinit var binding: ActivityEditdatapinjamBinding
    private val pinjamViewModel: PinjamViewModel by viewModels()
    private var pinjamId: Int = -1 // Inisialisasi dengan -1 untuk ID yang tidak valid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditdatapinjamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data from Intent
        pinjamId = intent.getIntExtra("id_pinjam", -1)
        val namaAnggota = intent.getStringExtra("namaanggota")
        val judulBuku = intent.getStringExtra("judulbuku_pinjam")
        val tanggalPinjam = intent.getStringExtra("tanggalpinjam")
        val tanggalKembali = intent.getStringExtra("tanggalkembali")

        // Populate the EditText fields
        binding.etNamaAnggota.setText(namaAnggota)
        binding.etJudulBuku.setText(judulBuku)
        binding.etTanggalPinjam.setText(tanggalPinjam)
        binding.etTanggalKembali.setText(tanggalKembali)

        // Setup Delete Button
        binding.btnHapus.setOnClickListener {
            if (pinjamId != -1) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val pinjamDao = PerpustakaanDatabase.getDatabase(this@editdatapinjam).daopinjam()
                        pinjamDao.deletePinjamById(pinjamId)

                        launch(Dispatchers.Main) {
                            Toast.makeText(this@editdatapinjam, "Buku berhasil dihapus", Toast.LENGTH_SHORT).show()
                            finish() // Kembali ke layar sebelumnya
                        }
                    } catch (e: Exception) {
                        launch(Dispatchers.Main) {
                            Toast.makeText(this@editdatapinjam, "Gagal menghapus Buku", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // Setup DatePicker untuk etTglPinjam
        binding.etTanggalPinjam.setOnClickListener {
            showDatePicker { date ->
                binding.etTanggalPinjam.setText(date)
            }
        }

        // Setup DatePicker untuk etTglKembali
        binding.etTanggalKembali.setOnClickListener {
            showDatePicker { date ->
                binding.etTanggalKembali.setText(date)
            }
        }

        // Set up the save button click listener
        binding.btnSimpan.setOnClickListener {
            if (validateInputs()) {
                if (pinjamId != -1) {
                    // Ambil data dari EditText
                    val updatedNamaAnggota = binding.etNamaAnggota.text.toString()
                    val updatedJudulBuku = binding.etJudulBuku.text.toString()
                    val updatedTanggalPinjam = binding.etTanggalPinjam.text.toString()
                    val updatedTanggalKembali = binding.etTanggalKembali.text.toString()

                    // Buat objek Pinjam dengan data baru
                    val updatedPinjam = Pinjam(pinjamId, updatedNamaAnggota, updatedJudulBuku, updatedTanggalPinjam, updatedTanggalKembali)

                    // Panggil metode di ViewModel untuk mengupdate data
                    pinjamViewModel.update(updatedPinjam)

                    Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke aktivitas sebelumnya
                } else {
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

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(date)
        }, year, month, day)

        datePickerDialog.show()
    }
}
