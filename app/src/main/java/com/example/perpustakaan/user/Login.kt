package com.example.perpustakaan.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perpustakaan.Dao.DaoUser
import com.example.perpustakaan.R
import com.example.perpustakaan.UserACTIVITY.UserHomeActivity
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var daftarButton: Button
    private lateinit var userDao: DaoUser

    private lateinit var adminIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.InputusrrEdit)
        passwordEditText = findViewById(R.id.InputpsssEdit)
        loginButton = findViewById(R.id.btnLogin)
        daftarButton = findViewById(R.id.btnRegis)
        adminIcon = findViewById(R.id.icon_ke_admin)

        val db = PerpustakaanDatabase.getDatabase(applicationContext)
        userDao = db.userDAO()

        daftarButton.setOnClickListener {
            val intent = Intent(this@Login, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Atur event klik untuk ikon admin
        adminIcon.setOnClickListener {
            val intent = Intent(this@Login, LoginAdmin::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch {
                val user = userDao.login(username, password, "USER")
                if (user != null) {
                    val intent = Intent(this@Login, UserHomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@Login, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
