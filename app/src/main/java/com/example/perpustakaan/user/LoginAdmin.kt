//package com.example.perpustakaan.user
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.example.perpustakaan.Dao.DaoUser
//import com.example.perpustakaan.UserACTIVITY.HomeActivity
//import com.example.perpustakaan.R
//import com.example.perpustakaan.database.PerpustakaanDatabase
//import com.google.android.material.textfield.TextInputEditText
//import kotlinx.coroutines.launch
//
//class LoginAdmin : AppCompatActivity() {
//
//    private lateinit var usernameEditText: TextInputEditText
//    private lateinit var passwordEditText: TextInputEditText
//    private lateinit var loginButton: Button
//    private lateinit var userDao: DaoUser
//
//    private lateinit var userIcon: ImageView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_loginadmin)
//
//        usernameEditText = findViewById(R.id.InputusrrEdit)
//        passwordEditText = findViewById(R.id.InputpsssEdit)
//        loginButton = findViewById(R.id.btnLogin)
//        userIcon = findViewById(R.id.icon_ke_user)
//
//        val db = PerpustakaanDatabase.getDatabase(applicationContext)
//        userDao = db.userDAO()
//
//        // Atur event klik untuk ikon user
//        userIcon.setOnClickListener {
//            val intent = Intent(this@LoginAdmin, Login::class.java)
//            startActivity(intent)
//        }
//
//        loginButton.setOnClickListener {
//            val username = usernameEditText.text.toString()
//            val password = passwordEditText.text.toString()
//
//            lifecycleScope.launch {
//                val user = userDao.login(username, password, "ADMIN")
//                if (user != null) {
//                    val intent = Intent(this@LoginAdmin, HomeActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                } else {
//                    Toast.makeText(this@LoginAdmin, "Invalid Admin credentials", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//}
