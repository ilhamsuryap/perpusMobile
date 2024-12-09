package com.example.perpustakaan.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.entity.Pinjam
import com.example.perpustakaan.repository.PeminjamanRepository
import kotlinx.coroutines.launch

class PeminjamanViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PeminjamanRepository
    val allPinjam: LiveData<List<Pinjam>>
    val uploadStatusPinjam: LiveData<Boolean>

    init {
        repository = PeminjamanRepository(application)
        allPinjam = repository.allPinjam
        uploadStatusPinjam = repository.uploadStatusPinjam
    }

    // Fungsi untuk menambahkan data pinjam
    fun insert(pinjam: Pinjam) {
        viewModelScope.launch {
            repository.insert(pinjam)
        }
    }

    // Fungsi untuk memperbarui data pinjam
    fun update(pinjam: Pinjam) {
        viewModelScope.launch {
            repository.update(pinjam)
        }
    }

    // Fungsi untuk menghapus data pinjam berdasarkan ID
    fun deleteById(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }
}
