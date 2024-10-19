package com.example.perpustakaan.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.repository.BukuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BukuViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BukuRepository
    val allBuku: LiveData<List<Buku>>

    init {
        val bukuDao = PerpustakaanDatabase.getDatabase(application).daobuku()
        repository = BukuRepository(bukuDao)
        allBuku = repository.allBuku
    }

    fun insert(buku: Buku) {
        // Meluncurkan coroutine di IO thread
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(buku)
        }
    }
}
