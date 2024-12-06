package com.example.perpustakaan.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.perpustakaan.entity.Pinjam

@Dao
interface DaoPinjam {

    @Insert
    suspend fun insert(pinjam: Pinjam)

    @Query("SELECT * FROM pinjam_table")
    fun getAllPinjam(): LiveData<List<Pinjam>>

    @Query("SELECT * FROM pinjam_table WHERE namaUser = :namaUser")
    suspend fun getPeminjamanByUser(namaUser: String): List<Pinjam>

    @Query("UPDATE pinjam_table SET statusKembali = 1 WHERE tanggalKembali <= :currentDate")
    suspend fun updateStatusKembali(currentDate: String)

    @Query("DELETE FROM pinjam_table WHERE id = :id")
    suspend fun deletePinjamById(id: Int)
}
