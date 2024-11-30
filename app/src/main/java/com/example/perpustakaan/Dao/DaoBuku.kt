package com.example.perpustakaan.Dao

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoBuku {

    @Insert
    suspend fun insert(buku: Buku)

    @Update
    suspend fun update(buku: Buku)

    @Delete
    suspend fun delete(buku: Buku)

    @Query("DELETE FROM buku_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM buku_table")
    fun getAllBuku(): LiveData<List<Buku>>

    @Query("DELETE FROM buku_table WHERE id = :id ")
    suspend fun deleteBukuById(id: Long)

    @Query("SELECT * FROM buku_table WHERE judul LIKE '%' || :query || '%'")
    suspend fun searchBukuByJudul(query: String): List<Buku>

    @Query("SELECT * FROM buku_table WHERE id = :id")
    suspend fun getBukuById(id: Long): Buku?
}
