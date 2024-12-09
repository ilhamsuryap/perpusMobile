package com.example.perpustakaan.Repository

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Dao.DaoBuku
import com.example.perpustakaan.database.PerpustakaanDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class BukuRepository(private val application: Application) {

    private val bukuDao: DaoBuku = PerpustakaanDatabase.getDatabase(application).daobuku()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val bukuRef = firebaseDatabase.getReference("buku")
    private val storageRef = firebaseStorage.reference.child("buku_images")
    private val lastIdRef = firebaseDatabase.getReference("last_book_id") // Referensi untuk ID terakhir buku

    // LiveData untuk semua buku
    val allBuku: LiveData<List<Buku>> = bukuDao.getAllBuku()


    // LiveData untuk status upload
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> = _uploadStatus

    // Fungsi untuk mendapatkan ID terakhir dari Firebase
    private suspend fun getLastBookId(): Int {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = lastIdRef.get().await()
                snapshot.getValue(Int::class.java) ?: 0 // Jika belum ada ID, mulai dari 0
            } catch (e: Exception) {
                e.printStackTrace()
                0 // Jika gagal, mulai dari 0
            }
        }
    }

    // Fungsi untuk memperbarui ID terakhir di Firebase
    private suspend fun updateLastBookId(newId: Int) {
        withContext(Dispatchers.IO) {
            lastIdRef.setValue(newId).await() // Update ID terakhir
        }
    }

    /**
     * Mengunggah gambar ke Firebase Storage
     */
    private suspend fun uploadImageToFirebase(imageUri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val fileName = UUID.randomUUID().toString()
                val fileRef = storageRef.child(fileName)

                val uploadTask = fileRef.putFile(imageUri).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await()

                downloadUrl.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Menambahkan buku ke Firebase Realtime Database dan Room
     */
    suspend fun insert(buku: Buku, imageUri: Uri? = null) {
        withContext(Dispatchers.IO) {
            try {
                // Ambil ID terakhir dan buat ID baru
                val lastId = getLastBookId()
                val newId = lastId + 1

                // Unggah gambar jika ada
                val imageUrl = imageUri?.let { uploadImageToFirebase(it) }

                // Update buku dengan ID baru dan URL gambar
                val bukuToSave = buku.copy(id = newId, gambarUrl = imageUrl ?: buku.gambarUrl)

                // Simpan ke Room
                bukuDao.insert(bukuToSave)

                // Simpan ke Firebase
                bukuRef.child(newId.toString()).setValue(bukuToSave)

                // Update ID terakhir di Firebase
                updateLastBookId(newId)

                _uploadStatus.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _uploadStatus.postValue(false)
            }
        }
    }

    // Metode CRUD lainnya...
    /**
     * Sinkronisasi data dari Firebase ke Room
     */
//    fun syncBukuFromFirebase() {
//        bukuRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val bukuList = snapshot.children.mapNotNull { it.getValue(Buku::class.java) }
//
//                // Launch a coroutine to insert all books in a batch
//                CoroutineScope(Dispatchers.IO).launch {
//                    bukuList.forEach { buku ->
//                        bukuDao.insert(buku) // Insert each book into Room
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//            }
//        })
//    }

    // Metode CRUD lainnya...
    suspend fun update(buku: Buku) {
        withContext(Dispatchers.IO) {
            // Update di Room
            bukuDao.update(buku)

            // Update di Firebase
            bukuRef.child(buku.id.toString()).setValue(buku)
        }
    }

    suspend fun delete(buku: Buku) {
        withContext(Dispatchers.IO) {
            // Hapus dari Room
            bukuDao.delete(buku)

            // Hapus dari Firebase
            bukuRef.child(buku.id.toString()).removeValue()
        }
    }

    // Pencarian buku
    suspend fun searchBukuByJudul(query: String): List<Buku> {
        return withContext(Dispatchers.IO) {
            bukuDao.searchBukuByJudul(query)
        }
    }

    // Menambahkan fungsi untuk mengambil semua buku
    suspend fun getAllBuku(): List<Buku> {
        return withContext(Dispatchers.IO) {
            bukuDao.getAllBukuSync() // Memanggil fungsi dari DAO untuk mendapatkan semua buku secara sinkron
        }
    }
}




