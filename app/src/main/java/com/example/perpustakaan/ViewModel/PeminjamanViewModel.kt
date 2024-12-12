package com.example.perpustakaan.ViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
    val allPinjam: LiveData<List<Pinjam>>


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
    // Sinkronisasi data dengan Firebase
    fun syncPinjam() = viewModelScope.launch(Dispatchers.IO) {
        try {
            pinjamRepository.syncWithFirebase()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    // Sinkronisasi data lokal (misalnya jika ada data yang belum disinkronkan)
    fun syncLocalDatabase(pinjamList: List<Pinjam>) = viewModelScope.launch(Dispatchers.IO) {
        try {
            pinjamRepository.syncLocalDatabase(pinjamList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    // Sinkronisasi data yang belum disinkronkan
    fun syncUnsyncedData() {
        viewModelScope.launch {
            try {
                pinjamRepository.syncUnsyncedData()
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
