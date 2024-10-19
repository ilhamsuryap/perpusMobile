package com.example.perpustakaan.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perpustakaan.R
import com.example.perpustakaan.database.PerpustakaanDatabase
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

        val db = PerpustakaanDatabase.getDatabase(applicationContext)
        userDao = db.userDAO()

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val existingUser = userDao.login(username, password)
                if (existingUser != null) {
                    Toast.makeText(this@Daftar, "Username sudah terdaftar", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = user(username = username, password = password)
                    userDao.insert(newUser)
                    Toast.makeText(this@Daftar, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@Daftar, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
