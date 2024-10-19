package com.example.perpustakaan.repository

import androidx.lifecycle.LiveData
import com.example.perpustakaan.Dao.DaoPinjam
import com.example.perpustakaan.entity.Pinjam

class PinjamRepository(private val pinjamDao: DaoPinjam) {
    val allPinjam: LiveData<List<Pinjam>> = pinjamDao.getAllPinjam()

    suspend fun insert(pinjam: Pinjam) {
        pinjamDao.insert_pinjam(pinjam)
    }
}