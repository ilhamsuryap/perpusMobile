package com.example.perpustakaan.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perpustakaan.Dao.DaoUser
import com.example.perpustakaan.HomeActivity
import com.example.perpustakaan.R
import com.example.perpustakaan.UserACTIVITY.UserHomeActivity

import com.example.perpustakaan.database.PerpustakaanDatabase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var DaftarButton: Button
    private lateinit var userDao: DaoUser

    private lateinit var roleRadioGroup: RadioGroup
    private lateinit var rbAdmin: RadioButton
    private lateinit var rbUser: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.InputusrrEdit)
        passwordEditText = findViewById(R.id.InputpsssEdit)
        loginButton = findViewById(R.id.btnLogin)
        DaftarButton = findViewById(R.id.btnRegis)

        roleRadioGroup = findViewById(R.id.radioGroupRole)
        rbAdmin = findViewById(R.id.rbAdmin)
        rbUser = findViewById(R.id.rbUser)

        val db = PerpustakaanDatabase.getDatabase(applicationContext)
        userDao = db.userDAO()

        DaftarButton.setOnClickListener {
            val intent = Intent(this@Login, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Mendapatkan role yang dipilih
            val selectedRole = when {
                rbAdmin.isChecked -> "ADMIN"
                rbUser.isChecked -> "USER"
                else -> null
            }

            if (selectedRole != null) {
                lifecycleScope.launch {
                    val user = userDao.login(username, password, selectedRole)
                    if (user != null) {
                        // Menentukan activity yang akan dibuka berdasarkan role
                        val intent = when (selectedRole) {
                            "ADMIN" -> Intent(this@Login, HomeActivity::class.java)
                            "USER" -> Intent(this@Login, UserHomeActivity::class.java)
                            else -> null
                        }
                        intent?.let {
                            startActivity(it)
                            finish()
                        }
                    } else {
                        Toast.makeText(this@Login, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@Login, "Please select a role", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
