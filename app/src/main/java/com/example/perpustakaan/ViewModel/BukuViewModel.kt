package com.example.perpustakaan.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Repository.BukuRepository
import kotlinx.coroutines.launch

class BukuViewModel(application: Application) : AndroidViewModel(application) {

    private val bukuRepository: BukuRepository = BukuRepository(application)

    // LiveData untuk semua buku
    val allBuku: LiveData<List<Buku>> = bukuRepository.allBuku

    // LiveData untuk hasil pencarian
    private val _searchResults = MutableLiveData<List<Buku>>()
    val searchResults: LiveData<List<Buku>> get() = _searchResults

    // Fungsi untuk mencari buku berdasarkan judul
    fun cariBuku(query: String) {
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                val results = bukuRepository.searchBukuByJudul(query)
                if (results.isNullOrEmpty()) {
                    // Jika tidak ada hasil, set LiveData dengan data kosong
                    _searchResults.postValue(emptyList())
                } else {
                    // Jika ada hasil, set LiveData dengan hasil pencarian
                    _searchResults.postValue(results)
                }
            }
        } else {
            // Jika query kosong, tampilkan semua buku
            _searchResults.postValue(emptyList())
        }
    }

    // Operasi CRUD
    fun insert(buku: Buku) = viewModelScope.launch {
        bukuRepository.insert(buku)
    }

    fun update(buku: Buku) = viewModelScope.launch {
        bukuRepository.update(buku)
    }

    fun delete(buku: Buku) = viewModelScope.launch {
        bukuRepository.delete(buku)
    }

    fun deleteAll() = viewModelScope.launch {
        bukuRepository.deleteAll()
    }
}
