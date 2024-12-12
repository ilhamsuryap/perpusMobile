package com.example.perpustakaan.Dao

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoBuku {

    // Insert a new book and return the generated primary key (Long)
    @Insert
    suspend fun insert(buku: Buku)

    // Update an existing book
    @Update
    suspend fun update(buku: Buku)

    // Delete a specific book
    @Delete
    suspend fun delete(buku: Buku)

    // Delete all books from the table
    @Query("DELETE FROM buku_table")
    suspend fun deleteAll()

    // Get all books ordered by title
    @Query("SELECT * FROM buku_table")
    fun getAllBuku(): LiveData<List<Buku>>

    // Delete a specific book by its ID
    @Query("DELETE FROM buku_table WHERE id = :id")
    suspend fun deleteBukuById(id: Int)

    // Search books by title (case-insensitive search)
    @Query("SELECT * FROM buku_table WHERE judul LIKE '%' || :query || '%' COLLATE NOCASE")
    suspend fun searchBukuByJudul(query: String): List<Buku>

    // Get a book by its ID
    @Query("SELECT * FROM buku_table WHERE id = :id")
    suspend fun getBukuById(id: Int): Buku?

    // Get all books ordered by title (synchronous)
    @Query("SELECT * FROM buku_table ORDER BY judul ASC")
    suspend fun getAllBukuSync(): List<Buku> // Added synchronous function

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bukuList: List<Buku>)

    @Query("SELECT * FROM buku_table WHERE syncronize = 0")
    suspend fun getUnsyncedBuku(): List<Buku>
}
