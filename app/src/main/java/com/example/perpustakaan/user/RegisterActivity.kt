package com.example.perpustakaan.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.perpustakaan.R
import com.example.perpustakaan.ViewModel.UserViewModel
import com.example.perpustakaan.entity.User
import com.example.perpustakaan.entity.UserRole
import com.google.firebase.FirebaseApp

class RegisterActivity : AppCompatActivity() {

    // Menggunakan ViewModel langsung tanpa ViewModelFactory
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        // Inisialisasi Firebase
        FirebaseApp.initializeApp(this)  // Menambahkan inisialisasi Firebase di sini

        val btnRegister = findViewById<Button>(R.id.btnDaftar)
        val roleSpinner = findViewById<Spinner>(R.id.spinnerRole)

        // Menyusun data untuk Spinner (Admin dan User)
        val roles = listOf("Admin", "User")  // Pilihan role
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        btnRegister.setOnClickListener {
            val username = findViewById<EditText>(R.id.inputuserEdit).text.toString()
            val password = findViewById<EditText>(R.id.inputpasEdit).text.toString()
            val selectedRole = roleSpinner.selectedItem.toString()

            // Menentukan role berdasarkan pilihan
            val role = if (selectedRole == "Admin") UserRole.ADMIN else UserRole.USER

            // Validasi input
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val user = User(username = username, password = password, role = role)

                    // Menyimpan data pengguna melalui ViewModel
                    userViewModel.insertUser(user)
                    Toast.makeText(this, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show()

                    // Mengarahkan ke LoginActivity setelah pendaftaran berhasil
                    val intent = Intent(this, Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    finish()  // Menutup activity Register setelah berpindah ke LoginActivity
                } catch (e: Exception) {
                    // Tangani exception jika ada error saat insert
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
