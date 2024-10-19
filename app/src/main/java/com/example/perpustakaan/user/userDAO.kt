package com.example.perpustakaan.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface userDAO {

    // Fungsi untuk memasukkan data user baru
    @Insert
    suspend fun insert(user: user)

    // Fungsi untuk mengecek apakah username sudah ada
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): user?

    // Fungsi login untuk mengecek kecocokan username dan password
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): user?
}
