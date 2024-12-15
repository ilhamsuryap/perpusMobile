package com.example.perpustakaan.ViewModel

import android.app.Application
import android.net.Uri
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

    val allBuku: LiveData<List<Buku>> // LiveData untuk semua buku
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> get() = _uploadStatus
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    init {
        val database = PerpustakaanDatabase.getDatabase(application)
        bukuRepository = BukuRepository(
            bukuDao = database.daobuku(),
            firebaseDatabase = FirebaseDatabase.getInstance(),
            networkHelper = NetworkHelper(application)
        )
        allBuku = bukuRepository.getAllBuku()
    }

    fun insertBooks(books: List<Buku>) {
        viewModelScope.launch(Dispatchers.IO) {
            bukuRepository.insertBooks(books)
        }
    }
    fun clearAllBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            bukuRepository.clearAllBooks()
        }
    }

    // Fungsi untuk memperbarui stok buku di Room
    fun updateRoom(buku: Buku) {
        viewModelScope.launch {
            bukuRepository.updateRoom(buku) // Memanggil fungsi di repository untuk mengupdate data buku
        }
    }

    // Fungsi untuk menambahkan buku
    fun insert(buku: Buku, imageUri: Uri? = null) = viewModelScope.launch {
        try {
            bukuRepository.insert(buku, imageUri)
            _uploadStatus.postValue(true) // Status berhasil
        } catch (e: Exception) {
            _uploadStatus.postValue(false) // Status gagal
        }
    }
    // Di BukuViewModel
    fun updateBuku(buku: Buku) {
        viewModelScope.launch(Dispatchers.IO) {
            bukuRepository.updateBuku(buku)  // Update data di Firebase dan Room
        }
    }





// Sinkronisasi data dengan Firebase

    // Fungsi untuk sinkronisasi buku dengan Firebase
    fun syncBuku(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            bukuRepository.syncBukuFromFirebaseToRoom()
            withContext(Dispatchers.Main) {
                onComplete?.invoke() // Callback dijalankan di thread utama
            }
        }
    }

    fun insertOrUpdate(buku: Buku, imageUri: Uri? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            bukuRepository.upsertBuku(buku)
        }
    }

    // Fungsi untuk menghapus buku
    fun deleteBuku(bukuId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            bukuRepository.deleteBuku(bukuId)
        }
    }

    // Fungsi untuk menghapus buku berdasarkan ID
    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            bukuRepository.deleteBuku(id)  // Memanggil fungsi deleteBuku di Repository
        }
    }



    fun getBukuByJudul(judul: String): LiveData<Buku?> {
        return bukuRepository.getBukuByJudul(judul)
    }

    // Fungsi untuk memperbarui stok buku
    fun incrementStock(idBuku: Int) {
        // Menggunakan Coroutine untuk operasi database
        viewModelScope.launch(Dispatchers.IO) {
            bukuRepository.updateStok(idBuku)
        }
    }

    fun getBukuById(id: Int): LiveData<Buku?> {
        return bukuRepository.getBukuById(id)
    }
    fun getAllBook(): LiveData<List<Buku>> {
        return bukuRepository.getAllBuku() // Memanggil fungsi di repository untuk mendapatkan data buku
    }
}





