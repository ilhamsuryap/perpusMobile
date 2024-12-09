package com.example.perpustakaan.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.perpustakaan.Dao.DaoPinjam
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.entity.Pinjam
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PeminjamanRepository(application: Application) {
    private val pinjamDao: DaoPinjam = PerpustakaanDatabase.getDatabase(application).daopinjam()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val pinjamRef = firebaseDatabase.getReference("pinjam")
    private val lastIdPinjamRef = firebaseDatabase.getReference("last_pinjam_id")

    // LiveData untuk semua data pinjam
    val allPinjam: LiveData<List<Pinjam>> = pinjamDao.getAllPinjam()

    // LiveData untuk status upload ke Firebase
    private val _uploadStatusPinjam = MutableLiveData<Boolean>()
    val uploadStatusPinjam: LiveData<Boolean> = _uploadStatusPinjam

    // Fungsi untuk mendapatkan ID terakhir dari Firebase
    private suspend fun getLastPinjamId(): Int {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = lastIdPinjamRef.get().await()
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
            try {
                lastIdPinjamRef.setValue(newId).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
                pinjamRef.child(newId.toString()).setValue(pinjamToSave).await()

                // Perbarui ID terakhir di Firebase
                updateLastPinjamId(newId)

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
}
