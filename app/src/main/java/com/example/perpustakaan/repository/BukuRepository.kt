package com.example.perpustakaan.Repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kasircafeapp.data.network.NetworkHelper
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Dao.DaoBuku
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class BukuRepository(
    // Pass application to NetworkHelper
    private val bukuDao: DaoBuku,
    private val firebaseDatabase: FirebaseDatabase,
    private val networkHelper: NetworkHelper

) {

    // Mendapatkan semua data dari database lokal
    fun getAllBuku(): LiveData<List<Buku>> =bukuDao.getAllBuku()

    private val _uploadStatus = MutableLiveData<Boolean>()

    private val bukuRef = firebaseDatabase.getReference("buku")


    // Get last book ID from Firebase to avoid conflicts when syncing
    private suspend fun getLastBookId(): Int {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = firebaseDatabase.getReference("last_book_id").get().await()
                snapshot.getValue(Int::class.java) ?: 0
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }

    private suspend fun updateLastBookId(newId: Int) {
        withContext(Dispatchers.IO) {
            firebaseDatabase.getReference("last_book_id").setValue(newId).await()
        }
    }

    // Fungsi untuk mengubah gambar menjadi base64 string


    suspend fun insert(buku: Buku, imageUri: Uri? = null) {
        withContext(Dispatchers.IO) {
            try {
                val lastId = getLastBookId()
                val newId = lastId + 1


                val bukuToSave = buku.copy(id = newId)

                bukuDao.insert(bukuToSave)

                if (networkHelper.isNetworkConnected()) {
                    // Save Buku to Firebase Realtime Database
                    bukuRef.child(newId.toString()).setValue(bukuToSave)
                    updateLastBookId(newId)
                }

                _uploadStatus.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _uploadStatus.postValue(false)
            }
        }
    }


    // Update Buku data in local database and Firebase
    // Memperbarui data
    suspend fun update(buku: Buku) {
        withContext(Dispatchers.IO) {
            // Update data di Firebase jika ada jaringan
            if (networkHelper.isNetworkConnected()) {
                val firebaseRef = firebaseDatabase.getReference("buku").child(buku.id.toString())
                firebaseRef.setValue(buku).await() // Firebase update
            }
            // Update data di Room Database
            bukuDao.update(buku)
        }
    }


    // Menghapus data
    suspend fun delete(buku: Buku) {
        withContext(Dispatchers.IO) {
            if (networkHelper.isNetworkConnected()) {
                val firebaseRef = firebaseDatabase.getReference("buku").child(buku.id.toString())
                firebaseRef.removeValue().await()
            }
            bukuDao.delete(buku)
        }
    }

    // Search Buku by title
    suspend fun searchBukuByJudul(query: String): List<Buku> {
        return withContext(Dispatchers.IO) {
            bukuDao.searchBukuByJudul(query)
        }
    }

    // Get all Buku from local database synchronously
    suspend fun getAllBook(): List<Buku> {
        return withContext(Dispatchers.IO) {
            bukuDao.getAllBukuSync()
        }
    }

    // Sync Buku data from Firebase to local database
    suspend fun syncWithFirebase() {
        if (networkHelper.isNetworkConnected()) {
            withContext(Dispatchers.IO) {
                val snapshot = bukuRef.get().await()
                val firebaseBukuList = mutableListOf<Buku>()
                snapshot.children.forEach {
                    val buku = it.getValue(Buku::class.java)
                    buku?.let { firebaseBukuList.add(it) }
                }
                syncLocalDatabase(firebaseBukuList)
            }
        }
    }

    // Sync local database with Firebase data
    suspend fun syncLocalDatabase(bukuList: List<Buku>) {
        withContext(Dispatchers.IO) {
            bukuDao.insertAll(bukuList)
        }
    }

    // Sync unsynced data from local database to Firebase
    suspend fun syncUnsyncedData() {
        if (networkHelper.isNetworkConnected()) {
            withContext(Dispatchers.IO) {
                val unsyncedBuku = bukuDao.getUnsyncedBuku()
                unsyncedBuku.forEach { buku ->
                    bukuRef.child(buku.id.toString()).setValue(buku).await()
                    bukuDao.update(buku.copy(syncronize = true))
                }
            }
        }
    }
}
