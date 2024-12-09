package com.example.perpustakaan.ViewModel

import android.app.Application
import android.net.Uri
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

    // LiveData untuk status upload
    val uploadStatus: LiveData<Boolean> = bukuRepository.uploadStatus

    // Fungsi untuk menambahkan buku
    fun insert(buku: Buku, imageUri: Uri? = null) = viewModelScope.launch {
        bukuRepository.insert(buku, imageUri)
    }

    // Fungsi untuk memperbarui buku
    fun update(buku: Buku) = viewModelScope.launch {
        bukuRepository.update(buku)
    }

    // Fungsi untuk menghapus buku
    fun delete(buku: Buku) = viewModelScope.launch {
        bukuRepository.delete(buku)
    }

    // Fungsi untuk mencari buku berdasarkan judul
    fun cariBuku(query: String) {
        viewModelScope.launch {
            if (query.isNotEmpty()) {
                // Jika query tidak kosong, cari buku berdasarkan judul
                val results = bukuRepository.searchBukuByJudul(query)
                _searchResults.postValue(results)
            } else {
                // Jika query kosong, tampilkan semua buku
                _searchResults.postValue(bukuRepository.getAllBuku())
            }
        }
    }



//    // Fungsi untuk sinkronisasi data dari Firebase ke Room
//    fun syncBukuFromFirebase() {
//        bukuRepository.syncBukuFromFirebase()
//    }
}
