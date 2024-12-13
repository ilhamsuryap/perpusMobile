package com.example.perpustakaan.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.entity.User
import com.example.perpustakaan.repository.UserRepository
import com.example.perpustakaan.database.PerpustakaanDatabase
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    init {
        // Inisialisasi DAO dan Repository
        val userDao = PerpustakaanDatabase.getDatabase(application).userDAO()
        userRepository = UserRepository(userDao)
    }

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    // Fungsi untuk menambahkan user baru
    fun insertUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.insertUser(user)
            } catch (e: Exception) {
                // Tangani jika ada error saat insert
            }
        }
    }

    // Fungsi login
    fun login(email: String, password: String, role: String) {
        viewModelScope.launch {
            try {
                _user.value = userRepository.login(email, password, role)
            } catch (e: Exception) {
                // Tangani jika ada error saat login
                _user.value = null
            }
        }
    }
}
