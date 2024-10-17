package com.example.perpustakaan.daftarbuku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.perpustakaan.R
import com.example.perpustakaan.databinding.ActivityDaftarBukuBinding
import com.example.perpustakaan.pinjam.FragmentFormDataPinjam

class DaftarBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBukuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup tombol tambah buku
        binding.btnTambahBuku.setOnClickListener {
            loadFragment(FragmentTambahDataBuku())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.daftarbuku.id, fragment)
        transaction.addToBackStack(null) // Optional, if you want to add the transaction to the back stack
        transaction.commit()
    }
}
