package com.example.perpustakaan.repository

import androidx.lifecycle.LiveData
import com.example.perpustakaan.Dao.DaoPinjam
import com.example.perpustakaan.entity.Pinjam

class PeminjamanRepository(private val pinjamDao: DaoPinjam) {
    val allPinjam: LiveData<List<Pinjam>> = pinjamDao.getAllPinjam()

    suspend fun insert(pinjam: Pinjam) {
        pinjamDao.insert(pinjam)
    }
    suspend fun update(pinjam: Pinjam) {
        pinjamDao.update(pinjam)
    }
    suspend fun DeleteById(id: Int) {
        pinjamDao.deletePinjamById(id)
    }

}