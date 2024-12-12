package com.example.perpustakaan.pinjamActivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.FragmentDikembalikan

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

    private val pinjamViewModel: PeminjamanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinjamBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        syncToFirebase()

        // Observing data changes
        pinjamViewModel.allPinjam.observe(this) { pinjamList ->
            pinjamAdapter.setData(pinjamList) // Update daftar pinjaman di adapter saat data berubah
        }

        // Set click listener for the button
        binding.btnPinjam.setOnClickListener {
            loadFragment(FragmentFormDataPinjam())
        }
    }

    private fun syncToFirebase() {
        val firebaseRef = FirebaseDatabase.getInstance().getReference("pinjam")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pinjamList = mutableListOf<Pinjam>()

                for (dataSnapshot in snapshot.children) {
                    val pinjam = dataSnapshot.getValue(Pinjam::class.java)
                    if (pinjam != null) {
                        pinjamList.add(pinjam)
                    }
                }

                // Perbarui adapter dengan daftar buku
                pinjamAdapter.setData(pinjamList)
                pinjamViewModel.syncLocalDatabase(pinjamList)
                pinjamViewModel.syncUnsyncedData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PinjamBukuActivity, "Gagal mengambil data dari Firebase", Toast.LENGTH_SHORT).show()
            }
        })
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

    private fun navigateToFragmentDikembalikan(pinjam: Pinjam) {
        val fragment = FragmentDikembalikan.newInstance(
            pinjam.judulbuku_pinjam,
            pinjam.tanggalpinjam,
            pinjam.tanggalkembali
        )
        loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.root.id, fragment) // Menggunakan root sebagai container untuk fragment
        transaction.addToBackStack(null) // Optional, menambahkan ke back stack jika diperlukan
        transaction.commit()
    }
}
