package com.example.perpustakaan.detailbuku

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.databinding.DetailbukuBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: DetailbukuBinding
    private val bukuViewModel: BukuViewModel by viewModels()  // Menggunakan ViewModel untuk data buku
    private var id: Int? = null // ID buku yang diterima dari intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding
        binding = DetailbukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil data dari Intent
        id = intent.getIntExtra("id", -1)

        if (id == -1) { // Memeriksa apakah ID valid
            Toast.makeText(this, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish() // Menutup activity jika buku tidak ditemukan
            return
        }

        // Memeriksa apakah pengguna adalah admin
        val isAdmin = checkIfUserIsAdmin()

        // Menyembunyikan tombol Edit dan Hapus jika bukan admin
        if (!isAdmin) {
            binding.btnEditBuku.visibility = View.GONE
            binding.btnHapusBuku.visibility = View.GONE
        }

        // Memuat detail buku menggunakan ViewModel
        loadBukuDetails()

        binding.btnHapusBuku.setOnClickListener {
            id?.let { id ->
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus buku ini?")
                    .setPositiveButton("Ya") { _, _ ->
                        // Menghapus data dari Firebase dan Room
                        deleteBukuFromFirebase(id.toString())
                        bukuViewModel.deleteBuku(id)  // Menghapus dari Room Database
                        Toast.makeText(this, "Buku berhasil dihapus", Toast.LENGTH_SHORT).show()
                        finish() // Menutup activity setelah penghapusan
                    }
                    .setNegativeButton("Tidak", null)
                    .create()
                dialog.show()
            }
        }
    }

    private fun checkIfUserIsAdmin(): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val role = sharedPreferences.getString("USER_ROLE", "USER")
        return role == "ADMIN"
    }

    private fun loadBukuDetails() {
        id?.let { id ->
            // Mengambil detail buku dari ViewModel (Room Database)
            bukuViewModel.getBukuById(id).observe(this) { buku ->
                if (buku != null) {
                    // Memperbarui UI dengan data buku yang diambil
                    binding.tvJudulBuku.text = buku.judul
                    binding.tvPenulis.text = buku.penulis
                    binding.tvTahunTerbit.text = buku.tahunTerbit.toString()
                    binding.tvDeskripsi.text = buku.deskripsi
                    binding.tvStok.text = buku.stok.toString()

                    // Menampilkan gambar menggunakan Glide
                    Glide.with(this)
                        .load(buku.gambarUrl)
                        .into(binding.imgBuku)
                } else {
                    // Jika buku tidak ditemukan, tampilkan pesan error
                    Log.e("DetailActivity", "Buku dengan ID $id tidak ditemukan")
                    Toast.makeText(this, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish()  // Menutup activity jika buku tidak ditemukan
                }
            }
        }
    }

    private fun deleteBukuFromFirebase(id: String) {
        val database = FirebaseDatabase.getInstance()
        val bukuRef = database.getReference("buku")  // Referensi ke node buku di Firebase

        bukuRef.orderByChild("id").equalTo(id.toDouble())  // Menyaring berdasarkan ID
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (bukuSnapshot in snapshot.children) {
                        bukuSnapshot.ref.removeValue()  // Menghapus data buku yang sesuai
                            .addOnSuccessListener {
                                Log.d("DetailActivity", "Buku berhasil dihapus dari Firebase")
                            }
                            .addOnFailureListener { e ->
                                Log.e("DetailActivity", "Gagal menghapus buku dari Firebase: ${e.message}")
                                Toast.makeText(this@DetailActivity, "Gagal menghapus buku dari Firebase", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DetailActivity", "Error membaca data: ${error.message}")
                }
            })
    }

}
