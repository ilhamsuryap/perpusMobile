package com.example.perpustakaan.pinjam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.perpustakaan.databinding.ActivityPinjamBukuBinding

class PinjamBukuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinjamBukuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinjamBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the button
        binding.btnPinjam.setOnClickListener {
            // Replace the current view with the com.example.perpustakaan.com.example.perpustakaan.pinjam.FragmentFormDataPinjam
            loadFragment(FragmentFormDataPinjam())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.pinjambuku.id, fragment)
        transaction.addToBackStack(null) // Optional, if you want to add the transaction to the back stack
        transaction.commit()
    }
}
