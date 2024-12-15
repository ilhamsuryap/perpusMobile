//package com.example.perpustakaan.ViewModel
//
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import com.example.perpustakaan.FragmentDikembalikanAdmin
//import com.example.perpustakaan.R
//import com.example.perpustakaan.adapter.PinjamAdapter
//import com.example.perpustakaan.databinding.ActivityAdminPengembalianBinding
//import com.example.perpustakaan.entity.Pinjam
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//class AdminPengembalian : AppCompatActivity() {
//    private lateinit var binding: ActivityAdminPengembalianBinding
//    private lateinit var pinjamAdapter: PinjamAdapter
//    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
//    private val pinjamViewModel: PeminjamanViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAdminPengembalianBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setupRecyclerView()
//        syncToFirebase()
//
//        pinjamViewModel.allPinjam.observe(this) { pinjamList ->
//            pinjamAdapter.setData(pinjamList)
//        }
//
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
//        swipeRefreshLayout.setOnRefreshListener {
//            refreshData()
//        }
//    }
//
//    private fun syncToFirebase() {
//        val firebaseRef = FirebaseDatabase.getInstance().getReference("pinjam")
//        firebaseRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val pinjamList = mutableListOf<Pinjam>()
//
//                for (dataSnapshot in snapshot.children) {
//                    val pinjam = dataSnapshot.getValue(Pinjam::class.java)
//                    if (pinjam != null) {
//                        pinjamList.add(pinjam)
//                    }
//                }
//
//                pinjamAdapter.setData(pinjamList)
//                pinjamViewModel.syncLocalDatabase(pinjamList)
//                pinjamViewModel.syncUnsyncedData()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@AdminPengembalian, "Gagal mengambil data dari Firebase", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun setupRecyclerView() {
//        pinjamAdapter = PinjamAdapter { pinjam ->
//            navigateToFragmentDikembalikanAdmin(pinjam)
//        }
//        binding.rvPinjam.apply {
//            layoutManager = GridLayoutManager(this@AdminPengembalian, 2)
//            adapter = pinjamAdapter
//        }
//    }
//
//    private fun navigateToFragmentDikembalikanAdmin(pinjam: Pinjam) {
//        val fragment = FragmentDikembalikanAdmin.newInstance(
//            pinjam.judulbuku_pinjam,
//            pinjam.tanggalpinjam,
//            pinjam.tanggalkembali
//        )
//        loadFragment(fragment)
//    }
//
//    private fun loadFragment(fragment: Fragment) {
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(binding.root.id, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }
//
//    private fun refreshData() {
//        swipeRefreshLayout.isRefreshing = false
//    }
//}
