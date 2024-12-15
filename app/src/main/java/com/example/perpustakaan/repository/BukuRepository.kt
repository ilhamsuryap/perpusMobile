package com.example.perpustakaan.Repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kasircafeapp.data.network.NetworkHelper
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Dao.DaoBuku
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class BukuRepository(
    private val bukuDao: DaoBuku,
    private val firebaseDatabase: FirebaseDatabase,
    private val networkHelper: NetworkHelper
) {
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: MutableLiveData<Boolean> get() = _uploadStatus

    private val bukuRef = firebaseDatabase.getReference("buku")


    // Fungsi untuk insert buku ke Room dan Firebase
    suspend fun insert(buku: Buku, imageUri: Uri? = null) {
        withContext(Dispatchers.IO) {
            try {
                // Cek apakah buku sudah ada di Room Database berdasarkan ID
                val existingBuku = bukuDao.getBukuById(buku.id)

                // Jika buku belum ada di Room
                if (existingBuku == null) {
                    // Sinkronisasi data ke Firebase terlebih dahulu
                    if (networkHelper.isNetworkConnected()) {
                        // Firebase otomatis meng-generate ID
                        val key = bukuRef.push().key // Mendapatkan ID otomatis dari Firebase
                        key?.let {
                            // Set ID yang di-generate oleh Firebase
                            val bukuWithGeneratedId = buku.copy(id = it.hashCode()) // Menggunakan hashCode() dari key Firebase sebagai ID
                            bukuRef.child(key).setValue(bukuWithGeneratedId).await() // Menyimpan ke Firebase
                        }
                    }

                    // Setelah berhasil disinkronkan ke Firebase, masukkan data ke Room Database (ID auto-generate oleh Room)
                    val bukuWithGeneratedId = buku.copy(id = 0) // Mengosongkan ID untuk Room auto-generate
                    bukuDao.insert(bukuWithGeneratedId)

                    _uploadStatus.postValue(true) // Status berhasil
                } else {
                    // Buku sudah ada, tidak perlu ditambahkan
                    _uploadStatus.postValue(false) // Buku sudah ada di Room Database
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uploadStatus.postValue(false) // Status gagal
            }
        }
    }

    // Fungsi untuk sinkronisasi data dari Firebase ke Room
    suspend fun syncBukuFromFirebaseToRoom() {
        if (networkHelper.isNetworkConnected()) {
            try {
                // Ambil semua data dari Firebase
                val snapshot = bukuRef.get().await()
                val bukuList = mutableListOf<Buku>()
                for (dataSnapshot in snapshot.children) {
                    val buku = dataSnapshot.getValue(Buku::class.java)
                    buku?.let { bukuList.add(it) }
                }

                // Simpan semua data ke Room
                if (bukuList.isNotEmpty()) {
                    bukuDao.deleteAllBuku() // Hapus semua data lama di Room
                    bukuDao.insertAll(bukuList) // Tambahkan data baru dari Firebase
                }
            } catch (e: Exception) {
                Log.e("BukuRepository", "Error syncing data: ${e.message}")
            }
        }
    }


    // Fungsi untuk menambahkan atau memperbarui data buku (Firebase + Room)
    suspend fun upsertBuku(buku: Buku,  imageUri: Uri? = null) {
        try {
            if (networkHelper.isNetworkConnected()) {
                // Simpan ke Firebase
                bukuRef.child(buku.id.toString()).setValue(buku).await()
            }
            // Simpan ke Room
            bukuDao.insert(buku)
        } catch (e: Exception) {
            Log.e("BukuRepository", "Error upserting data: ${e.message}")
        }
    }

    // Fungsi untuk menghapus data buku berdasarkan ID (Firebase + Room)
    suspend fun deleteBuku(bukuId: Int) {
        try {
            // Cek apakah ada koneksi internet
            if (networkHelper.isNetworkConnected()) {
                // Hapus dari Firebase
                bukuRef.child(bukuId.toString()).removeValue().await()
            }

            // Hapus dari Room
            bukuDao.deleteById(bukuId)
        } catch (e: Exception) {
            Log.e("BukuRepository", "Error deleting data: ${e.message}")
        }
    }


    // Fungsi untuk memperbarui buku
    suspend fun updateRoom(buku: Buku) {
        bukuDao.update(buku) // Memanggil fungsi DAO untuk mengupdate buku
    }
    // Di BukuRepository
    suspend fun updateBuku(buku: Buku) {
        try {
            if (networkHelper.isNetworkConnected()) {
                // Update data di Firebase
                bukuRef.child(buku.id.toString()).setValue(buku).await()
            }
            // Update data di Room
            bukuDao.update(buku)
        } catch (e: Exception) {
            Log.e("BukuRepository", "Error updating data: ${e.message}")
        }
    }

    // Fungsi untuk menghapus buku berdasarkan ID
    suspend fun delete(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                bukuDao.deleteBukuById(id)
                bukuRef.child(id.toString()).removeValue().await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun clearAllBooks() {
        bukuDao.clearAllBooks()
    }

    suspend fun insertBooks(books: List<Buku>) {
        bukuDao.insertBooks(books)
    }


    // Hapus data buku
    suspend fun delete(buku: Buku) {
        withContext(Dispatchers.IO) {
            try {
                bukuDao.delete(buku)
                if (networkHelper.isNetworkConnected()) {
                    bukuRef.child(buku.id.toString()).removeValue().await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fungsi untuk mendapatkan Buku berdasarkan ID
    fun getBukuById(id: Int): LiveData<Buku?> {
        return bukuDao.getBukuByIdPinjam(id)
    }

    // Mendapatkan semua buku dari database lokal
    // Fungsi untuk mendapatkan semua buku
    fun getAllBuku(): LiveData<List<Buku>> {
        return bukuDao.getAllBuku() // Memanggil fungsi DAO untuk mengambil semua data buku
    }
    fun getBukuByJudul(judul: String): LiveData<Buku?> {
        return bukuDao.getBukuByJudul(judul)
    }


    // Fungsi untuk memperbarui stok buku
    suspend fun updateStok(idBuku: Int) {
        // Ambil data buku berdasarkan ID
        val buku = bukuDao.getBukuById(idBuku)
        buku?.let {
            // Menambahkan 1 ke stok buku
            val stokBaru = it.stok + 1
            // Perbarui stok buku di Room
            it.stok = stokBaru
            bukuDao.update(it)  // Memanggil metode update pada DaoBuku
        }

    }

//    suspend fun syncBukuFromFirebase() {
//        // Mendapatkan referensi ke Firebase Database
//        val firebaseDatabase = FirebaseDatabase.getInstance()
//        val bukuRef = firebaseDatabase.getReference("buku")
//
//        try {
//            // Menunggu data buku dari Firebase
//            val snapshot = bukuRef.get().await()  // Menggunakan Firebase KTX untuk suspending function
//
//            // Membaca data dari snapshot Firebase dan mengonversinya ke objek Buku
//            val bukuList = mutableListOf<Buku>()
//            for (dataSnapshot in snapshot.children) {
//                val buku = dataSnapshot.getValue(Buku::class.java)
//                buku?.let {
//                    bukuList.add(it)
//                }
//            }
//
//            // Memperbarui data buku di Room jika ada data yang ditemukan di Firebase
//            if (bukuList.isNotEmpty()) {
//                // Menyimpan data buku ke dalam Room
//                bukuDao.insertAll(bukuList)
//            }
//        } catch (e: Exception) {
//            Log.e("BukuViewModel", "Gagal mengambil data buku dari Firebase: ${e.message}")
//        }
//    }
//
//}



//    // Sinkronisasi data Firebase ke loka
//        // Fungsi untuk mengambil semua buku dari Firebase
//    // Fungsi untuk mengambil semua buku dari Firebase
//    suspend fun syncBukuFromFirebase(): List<Buku> {
//        val bukuList = mutableListOf<Buku>()
//        val snapshot = bukuRef.get().await()  // Gunakan await() untuk menunggu hasil
//        for (bukuSnapshot in snapshot.children) {
//            val buku = bukuSnapshot.getValue(Buku::class.java)
//            buku?.let { bukuList.add(it) }
//        }
//        return bukuList
//    }
//
//    // Fungsi untuk menghapus semua buku di Room
//    suspend fun deleteAllBuku() {
//        bukuDao.deleteAllBuku()
//    }
//
//    // Fungsi untuk memperbarui atau menambah buku di Room
//    suspend fun upsertBuku(bukuList: List<Buku>) {
//        for (buku in bukuList) {
//            // Cek apakah buku sudah ada di Room berdasarkan ID
//            val existingBuku = bukuDao.getBukuById(buku.id)
//            if (existingBuku == null) {
//                // Jika buku belum ada, insert
//                bukuDao.insert(buku)
//            } else {
//                // Jika buku sudah ada, update
//                bukuDao.update(buku)
//            }
//        }
//    }
}















