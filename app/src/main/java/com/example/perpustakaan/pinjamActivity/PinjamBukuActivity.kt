package com.example.perpustakaan.pinjamActivity

import android.os.Bundle
import android.view.View
import android.widget.Button
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
    private lateinit var btnPinjam: Button
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

        binding.btnPinjam.setOnClickListener {
            loadFragment(FragmentFormDataPinjam())
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        val isUser = checkIfUserIsUser()

        if (!isUser) {
            findViewById<Button>(R.id.btnPinjam).visibility = View.GONE
        }
        btnPinjam = findViewById(R.id.btnPinjam)
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

                pinjamAdapter.setData(pinjamList)
                pinjamViewModel.syncLocalDatabase(pinjamList)
                pinjamViewModel.syncUnsyncedData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PinjamBukuActivity, "Gagal mengambil data dari Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkIfUserIsUser(): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val role = sharedPreferences.getString("USER_ROLE", "USER")
        return role == "ADMIN"
    }

    private fun setupRecyclerView() {
        pinjamAdapter = PinjamAdapter { pinjam ->
            navigateToFragmentDikembalikan(pinjam)
        }
        binding.rvPinjam.apply {
            layoutManager = GridLayoutManager(this@PinjamBukuActivity, 2)
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
        transaction.replace(binding.root.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun refreshData() {
        swipeRefreshLayout.isRefreshing = false
    }
}
