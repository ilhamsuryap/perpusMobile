package com.example.perpustakaan.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.entity.Pinjam
import com.example.perpustakaan.repository.PeminjamanRepository
import kotlinx.coroutines.launch

class PeminjamanViewModel(private val repository: PeminjamanRepository) : ViewModel() {

    // Menggunakan LiveData yang sudah dipantau oleh Repository
    val peminjamanList: LiveData<List<Pinjam>> = repository.allPinjam

    fun insert(pinjam: Pinjam) {
        viewModelScope.launch {
            repository.insertPeminjaman(pinjam)
        }
    }

    // Fungsi untuk menghapus pinjam berdasarkan id
    fun deletePinjamById(id: Int) {
        viewModelScope.launch {
            repository.deletePinjamById(id)
        }
    }
}
