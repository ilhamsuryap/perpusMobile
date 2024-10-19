package com.example.perpustakaan.repository

import androidx.lifecycle.LiveData
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Dao.DaoBuku

class BukuRepository(private val bukuDao: DaoBuku) {
    val allBuku: LiveData<List<Buku>> = bukuDao.getAllBuku()

    suspend fun insert(buku: Buku) {
        bukuDao.insert_buku(buku)
    }
}
