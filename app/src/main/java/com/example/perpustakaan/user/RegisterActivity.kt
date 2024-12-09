package com.example.perpustakaan.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
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
        val codeLabel = findViewById<TextView>(R.id.code_label)
        val inputCodeLayout = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputcod)
        val inputCodeEditText = findViewById<EditText>(R.id.inputcode)

        // Menyusun data untuk Spinner (Admin dan User)
        val roles = listOf("Admin", "User")  // Pilihan role
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        // Default visibilitas untuk kode konfirmasi
        codeLabel.visibility = View.GONE
        inputCodeLayout.visibility = View.GONE

        // Listener untuk Spinner
        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedRole = roles[position]
                if (selectedRole == "Admin") {
                    codeLabel.visibility = View.VISIBLE
                    inputCodeLayout.visibility = View.VISIBLE
                } else {
                    codeLabel.visibility = View.GONE
                    inputCodeLayout.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak ada tindakan yang diperlukan
            }
        }

        btnRegister.setOnClickListener {
            val username = findViewById<EditText>(R.id.inputuserEdit).text.toString()
            val password = findViewById<EditText>(R.id.inputpasEdit).text.toString()
            val selectedRole = roleSpinner.selectedItem.toString()
            val role = if (selectedRole == "Admin") UserRole.ADMIN else UserRole.USER

            // Validasi input
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                if (role == UserRole.ADMIN) {
                    val confirmationCode = inputCodeEditText.text.toString()
                    if (confirmationCode != "12345") { // Hardcode kode konfirmasi
                        Toast.makeText(this, "Kode konfirmasi salah", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }

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
