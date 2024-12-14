package com.example.perpustakaan.user


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.perpustakaan.UserACTIVITY.HomeActivity
import com.example.perpustakaan.UserACTIVITY.UserHomeActivity
import com.example.perpustakaan.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var daftarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Periksa apakah pengguna sudah login sebelumnya
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            checkUserRoleAndRedirect(currentUser.uid)
        }

        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.InputusrrEdit)
        passwordEditText = findViewById(R.id.InputpsssEdit)
        loginButton = findViewById(R.id.btnLogin)
        daftarButton = findViewById(R.id.btnRegis)

        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    checkUserRoleAndRedirect(userId)
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        daftarButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun checkUserRoleAndRedirect(userId: String) {
        database.child("users").child(userId).get().addOnSuccessListener { dataSnapshot ->
            val role = dataSnapshot.child("role").value.toString()

            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("USER_ROLE", role)  // Menyimpan role pengguna
            editor.apply()

            when (role) {
                "USER" -> {
                    startActivity(Intent(this, UserHomeActivity::class.java))
                    finish()
                }

                "ADMIN" -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }

                else -> {
                    Toast.makeText(this, "Role tidak dikenal", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mendapatkan data pengguna", Toast.LENGTH_SHORT).show()
        }
    }
}