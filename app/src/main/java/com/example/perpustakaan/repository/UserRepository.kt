package com.example.perpustakaan.repository

import com.example.perpustakaan.Dao.DaoUser
import com.example.perpustakaan.entity.User


class UserRepository(private val userDAO: DaoUser) {

    // Menambahkan user baru ke dalam database
    suspend fun insertUser(user: User) {
        userDAO.insert(user)
    }

    // Login user berdasarkan username, password, dan role
    suspend fun login(username: String, password: String, role: String): User? {
        return userDAO.login(username, password, role)
    }
}
