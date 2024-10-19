package com.example.perpustakaan.detailbuku

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.perpustakaan.R

class detail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailbuku)

        val judulBuku = intent.getStringExtra("BUKU_JUDUL")
        val penulisBuku = intent.getStringExtra("BUKU_PENULIS")
        val tahunTerbit = intent.getStringExtra("BUKU_TAHUN")
        val deskripsiBuku = intent.getStringExtra("BUKU_DESKRIPSI")

        findViewById<TextView>(R.id.tv_judul_buku).text = judulBuku
        findViewById<TextView>(R.id.tv_penulis).text = penulisBuku
        findViewById<TextView>(R.id.tv_tahun_terbit).text = tahunTerbit
        findViewById<TextView>(R.id.tv_deskripsi).text = deskripsiBuku
    }
}


