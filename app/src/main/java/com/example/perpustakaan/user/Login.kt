package com.example.perpustakaan.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perpustakaan.HomeActivity
import com.example.perpustakaan.R
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.pinjamActivity.PinjamBukuActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var DaftarButton: Button
    private lateinit var userDao: userDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.InputusrrEdit)
        passwordEditText = findViewById(R.id.InputpsssEdit)
        loginButton = findViewById(R.id.btnLogin)
        DaftarButton = findViewById(R.id.btnRegis)

        val db = PerpustakaanDatabase.getDatabase(applicationContext)
        userDao = db.userDAO()

        DaftarButton.setOnClickListener {
            val intent = Intent(this@Login, Daftar::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch {
                val user = userDao.login(username, password)
                if (user != null) {
                    val intent = Intent(this@Login, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@Login, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
