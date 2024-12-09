//package com.example.perpustakaan.ListBuku
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.cardview.widget.CardView
//import androidx.constraintlayout.widget.ConstraintLayout
//import com.example.perpustakaan.R
//import com.example.perpustakaan.detailbuku.DetailActivity
//
//class DataPinjam : AppCompatActivity() {
//
//    private lateinit var cardListBuku: ConstraintLayout
//    private lateinit var cardluarBuku: CardView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.style_daftar_buku)
//
//        cardListBuku = findViewById(R.id.cardListDaftarBuku)
//        cardluarBuku = findViewById(R.id.cardluarDaftarBuku)
//
//        cardListBuku.setOnClickListener {
//            val intent = Intent(this, DetailActivity::class.java)
//            startActivity(intent)
//        }
//        cardluarBuku.setOnClickListener {
//            val intent = Intent(this, DetailActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}
