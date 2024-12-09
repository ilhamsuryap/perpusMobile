package com.example.perpustakaan.UserACTIVITY

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.perpustakaan.R
import com.example.perpustakaan.pinjamActivity.PinjamBukuActivity

class ProfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.aboutUsButton).setOnClickListener {
            startActivity(Intent(this, TentangKamiActivity::class.java))
        }

        findViewById<Button>(R.id.helpButton).setOnClickListener {
            startActivity(Intent(this, BantuanActivity::class.java))
        }
    }

}