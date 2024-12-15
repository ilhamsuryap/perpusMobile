package com.example.perpustakaan.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.perpustakaan.entity.Pinjam

@Dao
interface DaoPinjam {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pinjam: Pinjam)

    @Update
    suspend fun update(pinjam: Pinjam)

    @Delete
    suspend fun delete(pinjam: Pinjam)

    @Query("DELETE FROM pinjam_table")
    suspend fun deleteAll()


    @Query("SELECT * FROM pinjam_table")
    fun getAllPinjam(): LiveData<List<Pinjam>>

    @Query("SELECT * FROM pinjam_table WHERE id_pinjam = :id")
    suspend fun getPinjamById(id: Int): Pinjam?

    @Query("SELECT * FROM pinjam_table WHERE namaanggota = :namaUser")
    suspend fun getPeminjamanByUser(namaUser: String): List<Pinjam>

//    @Query("UPDATE pinjam_table SET status = 1 WHERE Tanggal_Kembali <= :currentDate")
//    suspend fun updateStatusKembali(currentDate: String)

//    @Query("DELETE FROM pinjam_table WHERE id_pinjam = :idPinjam")
//    suspend fun deletePinjamById(idPinjam: Int)


    @Query("DELETE FROM pinjam_table WHERE id_pinjam = :idPinjam")
    suspend fun deletePinjamById(idPinjam: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(pinjamList: List<Pinjam>)


    @Query("DELETE FROM pinjam_table")
    suspend fun deleteAllPinjam() // Menghapus semua buku

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(pinjam: Pinjam)

    @Query("SELECT * FROM pinjam_table WHERE id_pinjam = :pinjamId LIMIT 1")
    suspend fun getPinjamById(pinjamId: String): Pinjam?




//    @Query("SELECT * FROM pinjam_table WHERE syncronize = 0")
//    suspend fun getUnsyncedPinjam(): List<Pinjam>


}
