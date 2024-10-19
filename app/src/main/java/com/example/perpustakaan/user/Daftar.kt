package com.example.perpustakaan.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perpustakaan.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class Daftar : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var userDao: userDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        usernameEditText = findViewById(R.id.inputuserEdit)
        passwordEditText = findViewById(R.id.inputpasEdit)
        registerButton = findViewById(R.id.btnDaftar)

        // Inisialisasi database dan DAO
        val db = AppDatabase.getDatabase(applicationContext)
        userDao = db.userDao()

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validasi input
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek apakah username sudah ada
            lifecycleScope.launch {
                val existingUser = userDao.login(username, password)
                if (existingUser != null) {
                    Toast.makeText(this@Daftar, "Username sudah terdaftar", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = user(username = username, password = password)
                    userDao.insert(newUser)
                    Toast.makeText(this@Daftar, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show()

                    // Pindah ke halaman login setelah pendaftaran berhasil
                    val intent = Intent(this@Daftar, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
