package com.example.perpustakaan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.ItemHome
import com.example.perpustakaan.R

class HomeActivity : AppCompatActivity() {

    private lateinit var notificationIcon: ImageView
    private lateinit var manageLibrary: TextView
    private lateinit var addBook: TextView
    private lateinit var listBooks: TextView
    private lateinit var borrowBook: TextView
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var AdapterHome: AdapterHome
    private lateinit var recentlyAddedBooks: MutableList<ItemHome>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize views
        notificationIcon = findViewById(R.id.notification)
        manageLibrary = findViewById(R.id.manage_library)
        addBook = findViewById(R.id.addbook)
        listBooks = findViewById(R.id.openbook)
        borrowBook = findViewById(R.id.borrowbook)
        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerViewHome)

        // Handle notification click event
        notificationIcon.setOnClickListener {
            val notificationIntent = Intent(this@HomeActivity, NotificationActivity::class.java)
            startActivity(notificationIntent)
        }

        // Initialize RecyclerView for recently added books
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // Sample data for the recently added books
        recentlyAddedBooks = mutableListOf(
            ItemHome(R.drawable.menara5negara,"Book Title 1", "Borrower 1"),
            ItemHome(R.drawable.menara5negara,"Book Title 2", "Borrower 2")
        )

        // Setup RecyclerView
        AdapterHome = AdapterHome(this, recentlyAddedBooks)
        recyclerView.layoutManager = GridLayoutManager(this, 2)  // 2 columns
        recyclerView.adapter = AdapterHome
    }
}
