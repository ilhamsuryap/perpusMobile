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
import com.example.perpustakaan.entity.Pinjam
import com.example.perpustakaan.repository.PeminjamanRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeminjamanViewModel(application: Application) : AndroidViewModel(application) {

    private val pinjamRepository: PeminjamanRepository
    private val _allPinjam = MutableLiveData<List<Pinjam>>()
    val allPinjam: LiveData<List<Pinjam>> // LiveData untuk semua buku


    init {
        // Inisialisasi Repository, Database, Network Helper
        val database = PerpustakaanDatabase.getDatabase(application)
        val networkHelper = NetworkHelper(application)
        pinjamRepository = PeminjamanRepository(
            pinjamDao = database.daopinjam(),
            firebaseDatabase = FirebaseDatabase.getInstance(),
            networkHelper = networkHelper
        )
        allPinjam = pinjamRepository.getAllPinjam()
    }

    // Fungsi untuk menambahkan data pinjam
    fun insert(pinjam: Pinjam) {
        viewModelScope.launch {
            pinjamRepository.insert(pinjam)
        }
    }

    // Fungsi untuk memperbarui data pinjam
    fun update(pinjam: Pinjam) {
        viewModelScope.launch {
            pinjamRepository.update(pinjam)
        }
    }

    // Fungsi untuk menghapus data pinjam berdasarkan ID
    fun deleteById(id: Int) {
        viewModelScope.launch {
            pinjamRepository.deleteById(id)
        }
    }

    fun syncPinjam(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            pinjamRepository.syncPinjamFromFirebaseToRoom()
            withContext(Dispatchers.Main) {
                onComplete?.invoke() // Callback dijalankan di thread utama
            }
        }
    }
    fun insertOrUpdate(pinjam: Pinjam) {
        viewModelScope.launch(Dispatchers.IO) {
            pinjamRepository.upsertPinjam(pinjam)
        }
    }
    fun deletePinjamById(idPinjam: Int) {
        viewModelScope.launch {
            pinjamRepository.deletePinjamById(idPinjam)  // Menggunakan repository untuk menghapus data
        }
    }
    fun deletePinjam(pinjamId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            pinjamRepository.deletePinjam(pinjamId)
        }
    }

}
