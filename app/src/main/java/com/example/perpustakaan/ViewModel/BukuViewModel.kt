package com.example.perpustakaan.ViewModel

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kasircafeapp.data.network.NetworkHelper
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Repository.BukuRepository
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BukuViewModel(application: Application) : AndroidViewModel(application) {


    private val bukuRepository: BukuRepository
    // LiveData untuk semua buku
    val allBuku: LiveData<List<Buku>>

    // LiveData untuk hasil pencarian
    private val _searchResults = MutableLiveData<List<Buku>>()
    val searchResults: LiveData<List<Buku>> get() = _searchResults

    // LiveData untuk status upload
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> get() = _uploadStatus

    init {
        // Inisialisasi Repository, Database, Network Helper
        val database = PerpustakaanDatabase.getDatabase(application)
        val networkHelper = NetworkHelper(application)
        bukuRepository = BukuRepository(
            bukuDao = database.daobuku(),
            firebaseDatabase = FirebaseDatabase.getInstance(),
            networkHelper = networkHelper
        )
        allBuku = bukuRepository.getAllBuku()
    }

    // Fungsi untuk menambahkan buku
    fun insert(buku: Buku, imageUri: Uri? = null) = viewModelScope.launch {
        try {
            bukuRepository.insert(buku, imageUri)
            _uploadStatus.postValue(true) // Jika berhasil
        } catch (e: Exception) {
            _uploadStatus.postValue(false) // Jika gagal
        }
    }

    // Fungsi untuk memperbarui buku
    fun update(buku: Buku) = viewModelScope.launch {
        try {
            bukuRepository.update(buku)
            _uploadStatus.postValue(true)
        } catch (e: Exception) {
            _uploadStatus.postValue(false)
        }
    }

    // Fungsi untuk menghapus buku
    fun delete(buku: Buku) = viewModelScope.launch {
        try {
            bukuRepository.delete(buku)
            _uploadStatus.postValue(true)
        } catch (e: Exception) {
            _uploadStatus.postValue(false)
        }
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
                _searchResults.postValue(bukuRepository.getAllBook())
            }
        }
    }



    // Sinkronisasi data dengan Firebase
    fun syncBuku() = viewModelScope.launch(Dispatchers.IO) {
        try {
            bukuRepository.syncWithFirebase()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Sinkronisasi data lokal (misalnya jika ada data yang belum disinkronkan)
    fun syncLocalDatabase(bukuList: List<Buku>) = viewModelScope.launch(Dispatchers.IO) {
        try {
            bukuRepository.syncLocalDatabase(bukuList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Sinkronisasi data yang belum disinkronkan
    fun syncUnsyncedData() {
        viewModelScope.launch {
            try {
                bukuRepository.syncUnsyncedData()
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Data offline berhasil disinkronkan!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Gagal menyinkronkan data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
