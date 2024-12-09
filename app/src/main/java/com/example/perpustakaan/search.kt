//package com.example.perpustakaan
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.recyclerview.widget.GridLayoutManager
//import com.example.perpustakaan.adapter.BukuAdapter
//import com.example.perpustakaan.ViewModel.BukuViewModel
//import com.example.perpustakaan.databinding.ActivitySearchBinding
//import androidx.activity.viewModels
//import androidx.appcompat.widget.SearchView
//import androidx.appcompat.widget.SearchView.OnQueryTextListener
//import java.util.*
//
//class search : AppCompatActivity() {
//
//    private lateinit var binding: ActivitySearchBinding
//    private lateinit var bukuAdapter: BukuAdapter
//    private val bukuViewModel: BukuViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySearchBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        // Initialize RecyclerView
//        setupRecyclerView()
//
//        // Observe LiveData from ViewModel to update book list
//        bukuViewModel.allBuku.observe(this) { bukuList ->
//            bukuAdapter.setData(bukuList)
//        }
//
//        // Set up SearchView
//        setupSearchView()
//    }
//
//    // Initialize RecyclerView and set up adapter
//    private fun setupRecyclerView() {
//        bukuAdapter = BukuAdapter { buku ->
//            // Define action when a book is clicked (if necessary)
//        }
//
//        binding.listbuku.apply {
//            layoutManager = GridLayoutManager(this@search, 2).apply {
//                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                    override fun getSpanSize(position: Int): Int {
//                        return if (bukuAdapter.currentList[position].stok == 0) 2 else 1
//                    }
//                }
//            }
//            adapter = bukuAdapter
//        }
//    }
//
//    // Set up SearchView for filtering books
//    private fun setupSearchView() {
//        binding.search.setOnQueryTextListener(object : OnQueryTextListener,
//            android.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                binding.search.clearFocus()
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                val searchText = newText?.lowercase(Locale.getDefault()).orEmpty()
//                bukuViewModel.allBuku.value?.let { allBooks ->
//                    val filteredBooks = allBooks.filter {
//                        it.judul.lowercase(Locale.getDefault()).contains(searchText) ||
//                                it.penulis.lowercase(Locale.getDefault()).contains(searchText)
//                    }
//                    bukuAdapter.setData(filteredBooks)
//                }
//                return true
//            }
//        })
//    }
//}
