package com.example.perpustakaan.repository

import androidx.lifecycle.LiveData
import com.example.perpustakaan.Dao.DaoPinjam
import com.example.perpustakaan.entity.Pinjam

class PeminjamanRepository(private val peminjamanDao: DaoPinjam) {

    // Mengambil semua data pinjam dari database dalam bentuk LiveData
    val allPinjam: LiveData<List<Pinjam>> = peminjamanDao.getAllPinjam()

    suspend fun insertPeminjaman(pinjam: Pinjam) {
        peminjamanDao.insert(pinjam)
    }

    suspend fun getPeminjamanByUser(namaUser: String): List<Pinjam> {
        return peminjamanDao.getPeminjamanByUser(namaUser)
    }

    suspend fun updateStatusKembali(currentDate: String) {
        peminjamanDao.updateStatusKembali(currentDate)
    }

    suspend fun deletePinjamById(id: Int) {
        peminjamanDao.deletePinjamById(id)
    }
}
