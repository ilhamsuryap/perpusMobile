package com.example.perpustakaan.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.entity.Pinjam
import com.example.perpustakaan.repository.PinjamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PinjamViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PinjamRepository
    val allPinjam: LiveData<List<Pinjam>>

    init {
        val pinjamDao = PerpustakaanDatabase.getDatabase(application).daopinjam()
        repository = PinjamRepository(pinjamDao)
        allPinjam = repository.allPinjam
    }

    // Fungsi untuk insert data pinjaman
    fun insert(pinjam: Pinjam) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(pinjam)
        }
    }

    // Fungsi untuk update data pinjaman
    fun update(pinjam: Pinjam) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(pinjam)
        }
    }

    // Fungsi untuk delete data pinjaman
    fun delete(pinjam: Pinjam) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(pinjam)
        }
    }
}
