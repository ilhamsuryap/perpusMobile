package com.example.perpustakaan.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perpustakaan.HomeActivity
import com.example.perpustakaan.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var userDao: userDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.InputusrrEdit)
        passwordEditText = findViewById(R.id.InputpsssEdit)
        loginButton = findViewById(R.id.btnLogin)

        // Inisialisasi database dan DAO
        val db = AppDatabase.getDatabase(applicationContext)
        userDao = db.userDao()

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch {
                val user = userDao.login(username, password)
                if (user != null) {
                    // Jika login berhasil, pindah ke halaman home atau activity lain
                    val intent = Intent(this@Login, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Jika login gagal
                    Toast.makeText(this@Login, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
