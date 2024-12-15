package com.example.perpustakaan.pinjamActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.FragmentDikembalikan
import com.example.perpustakaan.R

import com.example.perpustakaan.ViewModel.PeminjamanViewModel

import com.example.perpustakaan.adapter.PinjamAdapter
import com.example.perpustakaan.databinding.ActivityPinjamBukuBinding
import com.example.perpustakaan.entity.Pinjam
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PinjamBukuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinjamBukuBinding
    private lateinit var pinjamAdapter: PinjamAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val pinjamViewModel: PeminjamanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinjamBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Sinkronisasi data saat aplikasi dimulai
        pinjamViewModel.syncPinjam() {
            Toast.makeText(this, "Data berhasil disinkronisasi", Toast.LENGTH_SHORT).show()
        }

        // Observasi data dari Room
        pinjamViewModel.allPinjam.observe(this) { pinjamList ->
            updateRecyclerView(pinjamList)
        }

//        // Panggil syncPinjam() saat activity dimulai untuk sinkronisasi data dengan Firebase
//        pinjamViewModel.syncPinjam()

        // Set click listener for the button
        binding.btnPinjam.setOnClickListener {
            loadFragment(FragmentFormDataPinjam())
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            // Sinkronisasi data dengan Firebase ketika swipe refresh
            refreshData()
        }

        // Memeriksa apakah pengguna adalah admin
        val isUser = checkIfUserIsUser()

        // Menyembunyikan tombol Edit dan Hapus jika bukan admin
        if (!isUser) {
            binding.btnPinjam.visibility = View.GONE

        }
    }

    private fun setupRecyclerView() {
        pinjamAdapter = PinjamAdapter { pinjam ->
            navigateToFragmentDikembalikan(pinjam)
        }
        binding.rvPinjam.apply {
            layoutManager = GridLayoutManager(this@PinjamBukuActivity, 2) // Grid dengan 2 kolom
            adapter = pinjamAdapter
        }
    }

    private fun checkIfUserIsUser(): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val role = sharedPreferences.getString("USER_ROLE", "ADMIN")
        return role == "USER"
    }

    private fun navigateToFragmentDikembalikan(pinjam: Pinjam) {
        val fragment = FragmentDikembalikan()
        fragment.arguments = Bundle().apply {
            putInt("id", pinjam.id_pinjam)
            putString("judul_buku", pinjam.judulbuku_pinjam)
            putString("tanggal_pinjam", pinjam.tanggalpinjam)
            putString("tanggal_kembali", pinjam.tanggalkembali)
        }
        loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.root.id, fragment) // Menggunakan root sebagai container untuk fragment
        transaction.addToBackStack(null) // Optional, menambahkan ke back stack jika diperlukan
        transaction.commit()
    }

    private fun refreshData() {
        pinjamViewModel.syncPinjam() {
            // Menyembunyikan indikator refresh setelah data diperbarui
            swipeRefreshLayout.isRefreshing = false
            Toast.makeText(this, "Data diperbarui", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateRecyclerView(pinjamList: List<Pinjam>) {
        pinjamAdapter.setData(pinjamList)
    }
}
