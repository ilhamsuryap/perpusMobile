package com.example.perpustakaan.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class user(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String
)
