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
    suspend fun insert_pinjam(pinjam: Pinjam)

    @Update
    suspend fun update_pinjam(pinjam: Pinjam)

    @Delete
    suspend fun delete_pinjam(pinjam: Pinjam)

//    @Query("SELECT * FROM buku_table")
//    fun getallBuku() : LiveData<List<Buku>>

    @Query("DELETE FROM pinjam_table WHERE id_pinjam = :pinjamId")
    suspend fun deletePinjamById(pinjamId: Int)

    @Query("SELECT * FROM pinjam_table ORDER BY Nama_Anggota ASC")
    fun getAllPinjam(): LiveData<List<Pinjam>>

    @Query("SELECT * FROM pinjam_table WHERE status = 'dipinjam'")
    fun getBukuDipinjam(): LiveData<List<Pinjam>>

}
