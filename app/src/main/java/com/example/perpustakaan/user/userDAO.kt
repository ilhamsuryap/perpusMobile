package com.example.perpustakaan.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface userDAO {

    @Insert
    suspend fun insert(user: user)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): user?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): user?
}
