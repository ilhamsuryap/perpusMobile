package com.example.perpustakaan.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kasircafeapp.data.network.NetworkHelper
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Dao.DaoBuku
import com.example.perpustakaan.Dao.DaoPinjam
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.entity.Pinjam
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

//class PeminjamanRepository(
//    private val pinjamDao: DaoPinjam,
//    private val firebaseDatabase = FirebaseDatabase,
////    private val pinjamRef = firebaseDatabase.getReference("pinjam")
////            private val lastIdPinjamRef = firebaseDatabase.getReference("last_pinjam_id")
//            private val networkHelper: NetworkHelper
//) {
class PeminjamanRepository(
    // Pass application to NetworkHelper
    private val pinjamDao: DaoPinjam,
    private val firebaseDatabase: FirebaseDatabase,
    private val networkHelper: NetworkHelper

) {
    // Mendapatkan semua data dari database lokal
    fun getAllPinjam(): LiveData<List<Pinjam>> =pinjamDao.getAllPinjam()
    private val pinjamRef = firebaseDatabase.getReference("pinjam")
    // LiveData untuk semua data pinjam


    // LiveData untuk status upload ke Firebase
    private val _uploadStatusPinjam = MutableLiveData<Boolean>()
    val uploadStatusPinjam: LiveData<Boolean> = _uploadStatusPinjam

    // Fungsi untuk mendapatkan ID terakhir dari Firebase
    private suspend fun getLastPinjamId(): Int {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = firebaseDatabase.getReference("last_pinjam_id").get().await()
                snapshot.getValue(Int::class.java) ?: 0
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }

    // Fungsi untuk memperbarui ID terakhir di Firebase
    private suspend fun updateLastPinjamId(newId: Int) {
        withContext(Dispatchers.IO) {
            firebaseDatabase.getReference("last_pinjam_id").setValue(newId).await()
        }
    }

    // Fungsi untuk menambahkan data pinjam ke Room dan Firebase
    suspend fun insert(pinjam: Pinjam) {
        withContext(Dispatchers.IO) {
            try {
                // Ambil ID terakhir dan buat ID baru
                val lastId = getLastPinjamId()
                val newId = lastId + 1

                // Salin data pinjam dengan ID baru
                val pinjamToSave = pinjam.copy(id_pinjam = newId)

                // Simpan ke Room
                pinjamDao.insert(pinjamToSave)

                // Simpan ke Firebase
//                pinjamRef.child(newId.toString()).setValue(pinjamToSave).await()
                if (networkHelper.isNetworkConnected()) {
                    // Save Buku to Firebase Realtime Database
                    pinjamRef.child(newId.toString()).setValue(pinjamToSave)

                    // Perbarui ID terakhir di Firebase
                    updateLastPinjamId(newId)
                }
                // Update status upload berhasil
                _uploadStatusPinjam.postValue(true)

            } catch (e: Exception) {
                e.printStackTrace()
                // Update status upload gagal
                _uploadStatusPinjam.postValue(false)
            }
        }
    }

    // Fungsi untuk memperbarui data pinjam
    suspend fun update(pinjam: Pinjam) {
        withContext(Dispatchers.IO) {
            try {
                pinjamDao.update(pinjam)
                pinjamRef.child(pinjam.id_pinjam.toString()).setValue(pinjam).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fungsi untuk menghapus data pinjam berdasarkan ID
    suspend fun deleteById(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                pinjamDao.deletePinjamById(id)
                pinjamRef.child(id.toString()).removeValue().await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    // Sync Buku data from Firebase to local database
    suspend fun syncWithFirebase() {
        if (networkHelper.isNetworkConnected()) {
            withContext(Dispatchers.IO) {
                val snapshot = pinjamRef.get().await()
                val firebasePinjamList = mutableListOf<Pinjam>()
                snapshot.children.forEach {
                    val buku = it.getValue(Pinjam::class.java)
                    buku?.let { firebasePinjamList.add(it) }
                }
                syncLocalDatabase(firebasePinjamList)
            }
        }
    }
    // Sync local database with Firebase data
    suspend fun syncLocalDatabase(pinjamList: List<Pinjam>) {
        withContext(Dispatchers.IO) {
            pinjamDao.insertAll(pinjamList)
        }
    }
    suspend fun syncUnsyncedData() {
        if (networkHelper.isNetworkConnected()) {
            withContext(Dispatchers.IO) {
                val unsyncedPinjam = pinjamDao.getUnsyncedPinjam()
                unsyncedPinjam.forEach { pinjam ->
                    pinjamRef.child(pinjam.id_pinjam.toString()).setValue(pinjam).await()
                    pinjamDao.update(pinjam.copy(syncronize = true))
                }
            }
        }
    }
}
