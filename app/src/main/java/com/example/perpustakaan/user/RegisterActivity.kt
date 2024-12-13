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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        auth = FirebaseAuth.getInstance()

        val btnRegister = findViewById<Button>(R.id.btnDaftar)
        val roleSpinner = findViewById<Spinner>(R.id.spinnerRole)
        val codeLabel = findViewById<TextView>(R.id.code_label)
        val inputCodeLayout =
            findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputcod)
        val inputCodeEditText = findViewById<EditText>(R.id.inputcode)

        val roles = listOf("Admin", "User")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        codeLabel.visibility = View.GONE
        inputCodeLayout.visibility = View.GONE

        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (roles[position] == "Admin") {
                    codeLabel.visibility = View.VISIBLE
                    inputCodeLayout.visibility = View.VISIBLE
                } else {
                    codeLabel.visibility = View.GONE
                    inputCodeLayout.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnRegister.setOnClickListener {
            val Email = findViewById<EditText>(R.id.inputuserEdit).text.toString()
            val password = findViewById<EditText>(R.id.inputpasEdit).text.toString()
            val selectedRole = roleSpinner.selectedItem.toString()

            if (Email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                Toast.makeText(this, "Masukkan email yang valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Memeriksa panjang password
            if (password.length < 6) {
                Toast.makeText(this, "Password harus terdiri dari minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedRole == "Admin") {
                val confirmationCode = inputCodeEditText.text.toString()
                if (confirmationCode != "12345") {
                    Toast.makeText(this, "Kode konfirmasi salah", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            auth.createUserWithEmailAndPassword(Email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val role = if (selectedRole == "Admin") UserRole.ADMIN else UserRole.USER
                    val userId = auth.currentUser?.uid ?: ""
                    database.child("users").child(userId)
                        .setValue(mapOf("Email" to Email, "role" to role.name))
                    Toast.makeText(this, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }    }