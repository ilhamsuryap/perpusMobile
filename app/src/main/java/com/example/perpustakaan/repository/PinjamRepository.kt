package com.example.perpustakaan.repository

import androidx.lifecycle.LiveData
import com.example.perpustakaan.Dao.DaoPinjam

import com.example.perpustakaan.entity.Pinjam

class PinjamRepository(private val pinjamDao: DaoPinjam) {

    val allPinjam: LiveData<List<Pinjam>> = pinjamDao.getAllPinjam()

    // Fungsi untuk insert data pinjaman
    suspend fun insert(pinjam: Pinjam) {
        pinjamDao.insert_pinjam(pinjam)
    }

    // Fungsi untuk update data pinjaman
    suspend fun update(pinjam: Pinjam) {
        pinjamDao.update_pinjam(pinjam)
    }

    // Fungsi untuk delete data pinjaman
    suspend fun delete(pinjam: Pinjam) {
        pinjamDao.delete_pinjam(pinjam)
    }
}
