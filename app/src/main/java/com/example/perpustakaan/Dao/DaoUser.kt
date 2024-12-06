package com.example.perpustakaan.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.perpustakaan.entity.User

@Dao
interface DaoUser {

    // Fungsi untuk menyimpan user baru
    @Insert
    suspend fun insert(user: User)

    // Fungsi untuk login, memverifikasi username, password, dan role
    // Menemukan user berdasarkan username, password, dan role
    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password AND role = :role LIMIT 1")
    suspend fun login(username: String, password: String, role: String): User?

    // Fungsi untuk mengecek apakah username sudah terdaftar
    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): User?
}
