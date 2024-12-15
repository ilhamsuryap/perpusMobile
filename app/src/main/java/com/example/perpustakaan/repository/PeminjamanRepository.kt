package com.example.perpustakaan.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kasircafeapp.data.network.NetworkHelper
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Dao.DaoPinjam
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.example.perpustakaan.entity.Pinjam
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PeminjamanRepository(
    private val pinjamDao: DaoPinjam,
    private val firebaseDatabase: FirebaseDatabase,
    private val networkHelper: NetworkHelper
) {
    // Mendapatkan semua data dari database lokal
    fun getAllPinjam(): LiveData<List<Pinjam>> = pinjamDao.getAllPinjam()

    private val pinjamRef = firebaseDatabase.getReference("pinjam")

    // LiveData untuk status upload ke Firebase
    private val _uploadStatusPinjam = MutableLiveData<Boolean>()
    val uploadStatusPinjam: LiveData<Boolean> = _uploadStatusPinjam

    // Fungsi untuk insert buku ke Room dan Firebase
    suspend fun insert(pinjam: Pinjam) {
        withContext(Dispatchers.IO) {
            try {
                // Cek apakah buku sudah ada di Room Database berdasarkan ID
                val existingPinjam = pinjamDao.getPinjamById(pinjam.id_pinjam)

                // Jika buku belum ada di Room
                if (existingPinjam == null) {
                    // Sinkronisasi data ke Firebase terlebih dahulu
                    if (networkHelper.isNetworkConnected()) {
                        // Firebase otomatis meng-generate ID
                        val key = pinjamRef.push().key // Mendapatkan ID otomatis dari Firebase
                        key?.let {
                            // Set ID yang di-generate oleh Firebase
                            val pinjamWithGeneratedId = pinjam.copy(id_pinjam = it.hashCode()) // Menggunakan hashCode() dari key Firebase sebagai ID
                            pinjamRef.child(key).setValue(pinjamWithGeneratedId).await() // Menyimpan ke Firebase
                        }
                    }

                    // Setelah berhasil disinkronkan ke Firebase, masukkan data ke Room Database (ID auto-generate oleh Room)
                    val pinjamWithGeneratedId = pinjam.copy(id_pinjam = 0) // Mengosongkan ID untuk Room auto-generate
                    pinjamDao.insert(pinjamWithGeneratedId)

                    _uploadStatusPinjam.postValue(true) // Status berhasil
                } else {
                    // Buku sudah ada, tidak perlu ditambahkan
                    _uploadStatusPinjam.postValue(false) // Buku sudah ada di Room Database
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uploadStatusPinjam.postValue(false) // Status gagal
            }
        }
    }

    suspend fun syncPinjamFromFirebaseToRoom() {
        if (networkHelper.isNetworkConnected()) {
            try {
                // Ambil semua data dari Firebase
                val snapshot = pinjamRef.get().await()
                val pinjamList = mutableListOf<Pinjam>()
                for (dataSnapshot in snapshot.children) {
                    val pinjam = dataSnapshot.getValue(Pinjam::class.java)
                    pinjam?.let { pinjamList.add(it) }
                }

                // Simpan semua data ke Room
                if (pinjamList.isNotEmpty()) {
                    // Hapus semua data lama di Room dan masukkan data baru
                    pinjamDao.deleteAllPinjam()
                    pinjamDao.insertAll(pinjamList)
                }
            } catch (e: Exception) {
                Log.e("PinjamRepository", "Error syncing data: ${e.message}")
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

    // Fungsi untuk menambahkan atau memperbarui data buku (Firebase + Room)
    suspend fun upsertPinjam(pinjam: Pinjam) {
        try {
            if (networkHelper.isNetworkConnected()) {
                // Simpan ke Firebase
                pinjamRef.child(pinjam.id_pinjam.toString()).setValue(pinjam).await()
            }
            // Simpan ke Room
            pinjamDao.insert(pinjam)
        } catch (e: Exception) {
            Log.e("PinjamRepository", "Error upserting data: ${e.message}")
        }
    }

    // Fungsi untuk menghapus data buku berdasarkan ID (Firebase + Room)
    suspend fun deletePinjam(pinjamId: Int) {
        try {
            if (networkHelper.isNetworkConnected()) {
                // Hapus dari Firebase
                pinjamRef.child(pinjamId.toString()).removeValue().await()
            }
            // Hapus dari Room
            pinjamDao.deletePinjamById(pinjamId)
        } catch (e: Exception) {
            Log.e("PinjamRepository", "Error deleting data: ${e.message}")
        }
    }



    // Di BukuRepository
    suspend fun updatePinjam(pinjam: Pinjam) {
        try {
            if (networkHelper.isNetworkConnected()) {
                // Update data di Firebase
                pinjamRef.child(pinjam.id_pinjam.toString()).setValue(pinjam).await()
            }
            // Update data di Room
            pinjamDao.update(pinjam)
        } catch (e: Exception) {
            Log.e("PinjamRepository", "Error updating data: ${e.message}")
        }
    }

    // Sinkronisasi data Pinjam dari Firebase ke database lokal
//    // Sinkronisasi data Firebase ke lokal
//    suspend fun syncPinjamFromFirebase() {
//        pinjamRef.get().addOnSuccessListener { snapshot ->
//            snapshot.children.forEach { child ->
//                val pinjam = child.getValue(Pinjam::class.java)
//                if (pinjam != null) {
//                    // Masukkan data ke database lokal
//                    GlobalScope.launch {
//                        pinjamDao.insert(pinjam)
//                    }
//                }
//            }
//        }
//    }

//    // Fungsi untuk mengambil semua buku dari Firebase
//    // Fungsi untuk mengambil semua buku dari Firebase
//    suspend fun syncPinjamFromFirebase(): List<Pinjam> {
//        val pinjamList = mutableListOf<Pinjam>()
//        val snapshot = pinjamRef.get().await()  // Gunakan await() untuk menunggu hasil
//        for (pinjamSnapshot in snapshot.children) {
//            val pinjam = pinjamSnapshot.getValue(Pinjam::class.java)
//            pinjam?.let { pinjamList.add(it) }
//        }
//        return pinjamList
//    }

    suspend fun deletePinjamById(idPinjam: Int) {
        pinjamDao.deletePinjamById(idPinjam)
    }

    // Fungsi untuk memperbarui atau menambah buku di Room
    suspend fun upsertPinjam(pinjamList: List<Pinjam>) {
        for (pinjam in pinjamList) {
            // Cek apakah pinjam sudah ada di Room berdasarkan ID
            val existingPinjam = pinjamDao.getPinjamById(pinjam.id_pinjam)
            if (existingPinjam == null) {
                // Jika pinjam belum ada, insert
                pinjamDao.insert(pinjam)
            } else {
                // Jika pinjam sudah ada, update
                pinjamDao.update(pinjam)
            }
        }
    }

//    // Fungsi untuk sinkronisasi data dari Firebase ke Room (dijalankan sekali saat aplikasi dijalankan)
//    suspend fun syncPinjamFromFirebaseToRoom() {
//        if (networkHelper.isNetworkConnected()) {
//            try {
//                // Ambil semua data dari Firebase
//                val snapshot = pinjamRef.get().await()
//                val pinjamList = mutableListOf<Pinjam>()
//                for (dataSnapshot in snapshot.children) {
//                    val pinjam = dataSnapshot.getValue(Pinjam::class.java)
//                    pinjam?.let { pinjamList.add(it) }
//                }
//
//                // Simpan semua data ke Room
//                if (pinjamList.isNotEmpty()) {
//                    pinjamDao.deleteAllPinjam() // Hapus semua data lama di Room
//                    pinjamDao.insertAll(pinjamList) // Tambahkan data baru dari Firebase
//                }
//            } catch (e: Exception) {
//                Log.e("BukuRepository", "Error syncing data: ${e.message}")
//            }
//        }
//    }

    // Fungsi untuk menambahkan atau memperbarui data buku (Firebase + Room)
    suspend fun upsertPinjam(pinjam: Pinjam,  imageUri: Uri? = null) {
        try {
            if (networkHelper.isNetworkConnected()) {
                // Simpan ke Firebase
                pinjamRef.child(pinjam.id_pinjam.toString()).setValue(pinjam).await()
            }
            // Simpan ke Room
            pinjamDao.insert(pinjam)
        } catch (e: Exception) {
            Log.e("BukuRepository", "Error upserting data: ${e.message}")
        }
    }

    // Fungsi untuk menghapus data buku berdasarkan ID (Firebase + Room)
    suspend fun deleteBuku(pinjamId: Int) {
        try {
            if (networkHelper.isNetworkConnected()) {
                // Hapus dari Firebase
                pinjamRef.child(pinjamId.toString()).removeValue().await()
            }
            // Hapus dari Room
            pinjamDao.deletePinjamById(pinjamId)
        } catch (e: Exception) {
            Log.e("BukuRepository", "Error deleting data: ${e.message}")
        }
    }


}
