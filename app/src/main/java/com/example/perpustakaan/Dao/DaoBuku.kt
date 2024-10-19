package com.example.perpustakaan.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoBuku {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert_buku(buku: Buku)

    @Update
    suspend fun update_buku(buku: Buku)

    @Delete
    suspend fun delete_buku(buku: Buku)

//    @Query("SELECT * FROM buku_table")
//    fun getallBuku() : LiveData<List<Buku>>

    @Query("SELECT * FROM buku_table ORDER BY judul_buku ASC")
    fun getAllBuku(): LiveData<List<Buku>>
}