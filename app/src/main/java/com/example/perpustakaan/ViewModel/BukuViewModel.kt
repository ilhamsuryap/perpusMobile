package com.example.perpustakaan.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.repository.BukuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BukuViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BukuRepository
    val allBuku: LiveData<List<Buku>>

    private val _allBuku = MutableLiveData<List<Buku>>()
    val buku : LiveData<List<Buku>> get() = _allBuku


    // Tambahkan LiveData baru di ViewModel untuk pencarian
    private val _searchResults = MutableLiveData<List<Buku>>()
    val searchResults: LiveData<List<Buku>> get() = _searchResults

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
//
//    fun cariBuku(namaBuku: String) {
//        viewModelScope.launch {
//            _allBuku.postValue(repository.cariBuku(namaBuku)) // Menggunakan postValue untuk mengupdate LiveData
//        }
//    }

    fun cariBuku(query: String) {
        viewModelScope.launch {
            val result = repository.cariBuku(query)  // Pastikan search query berfungsi di Dao
            _searchResults.postValue(result)
        }
    }


}
