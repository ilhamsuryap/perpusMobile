package com.example.perpustakaan.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Dao.DaoBuku
import com.example.perpustakaan.database.PerpustakaanDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BukuRepository(application: Application) {

    private val bukuDao: DaoBuku = PerpustakaanDatabase.getDatabase(application).daobuku()

    // LiveData untuk semua buku
    val allBuku: LiveData<List<Buku>> = bukuDao.getAllBuku()

    /**
     * Menambahkan buku ke database
     * @param buku Buku yang akan ditambahkan
     */
    suspend fun insert(buku: Buku) {
        withContext(Dispatchers.IO) {
            runCatching {
                bukuDao.insert(buku)
            }.onFailure { e ->
                e.printStackTrace() // Logging error jika terjadi
            }
        }
    }

    /**
     * Memperbarui data buku di database
     * @param buku Buku yang akan diperbarui
     */
    suspend fun update(buku: Buku) {
        withContext(Dispatchers.IO) {
            runCatching {
                bukuDao.update(buku)
            }.onFailure { e ->
                e.printStackTrace() // Logging error jika terjadi
            }
        }
    }

    /**
     * Menghapus buku tertentu dari database
     * @param buku Buku yang akan dihapus
     */
    suspend fun delete(buku: Buku) {
        withContext(Dispatchers.IO) {
            runCatching {
                bukuDao.delete(buku)
            }.onFailure { e ->
                e.printStackTrace() // Logging error jika terjadi
            }
        }
    }

    /**
     * Menghapus semua buku dari database
     */
    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            runCatching {
                bukuDao.deleteAll()
            }.onFailure { e ->
                e.printStackTrace() // Logging error jika terjadi
            }
        }
    }

    /**
     * Mencari buku berdasarkan judul
     * @param query Kata kunci pencarian
     * @return List buku yang sesuai
     */
    suspend fun searchBukuByJudul(query: String): List<Buku> {
        return withContext(Dispatchers.IO) {
            runCatching {
                bukuDao.searchBukuByJudul(query)
            }.getOrElse { e ->
                e.printStackTrace() // Logging error jika terjadi
                emptyList() // Kembalikan list kosong jika terjadi error
            }
        }
    }
}
