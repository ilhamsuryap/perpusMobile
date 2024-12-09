package com.example.perpustakaan.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.perpustakaan.entity.Pinjam

@Dao
interface DaoPinjam {

    @Insert
    suspend fun insert(pinjam: Pinjam)

    @Update
    suspend fun update(pinjam: Pinjam)

    @Delete
    suspend fun delete(pinjam: Pinjam)

    @Query("DELETE FROM pinjam_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM pinjam_table")
    fun getAllPinjam(): LiveData<List<Pinjam>>

    @Query("SELECT * FROM pinjam_table WHERE namaanggota = :namaUser")
    suspend fun getPeminjamanByUser(namaUser: String): List<Pinjam>

//    @Query("UPDATE pinjam_table SET status = 1 WHERE Tanggal_Kembali <= :currentDate")
//    suspend fun updateStatusKembali(currentDate: String)

    @Query("DELETE FROM pinjam_table WHERE id_pinjam = :id")
    suspend fun deletePinjamById(id: Int)
}
