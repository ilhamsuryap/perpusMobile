package com.example.perpustakaan.Dao

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoBuku {

    // Insert a new book and return the generated primary key (Long)
    @Insert(onConflict = OnConflictStrategy.REPLACE)  // Agar jika ada duplikat ID, data yang baru menggantikan yang lama
    suspend fun insert(buku: Buku)

    @Query("SELECT * FROM buku_table WHERE id = :bukuId LIMIT 1")
    suspend fun getBukuById(bukuId: String): Buku?

    @Update
    suspend fun update(buku: Buku)

    @Query("DELETE FROM buku_table")
    suspend fun clearAllBooks()
    // Delete a specific book

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Buku>)

    @Delete
    suspend fun delete(buku: Buku)

    // Query untuk menghapus buku berdasarkan ID
    @Query("DELETE FROM buku_table WHERE id = :bukuId")
    suspend fun deleteById(bukuId:Int )

    // Delete all books from the table
    @Query("DELETE FROM buku_table")
    suspend fun deleteAll()

    // Get all books ordered by title
    @Query("SELECT * FROM buku_table")
    fun getAllBuku(): LiveData<List<Buku>>

    @Query("SELECT * FROM buku_table") // Query untuk mengambil semua data buku
    suspend fun getAllData(): List<Buku>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(buku: Buku)
    // Delete a specific book by its ID
    @Query("DELETE FROM buku_table WHERE id = :id")
    suspend fun deleteBukuById(id: Int)

    // Search books by title (case-insensitive search)
    @Query("SELECT * FROM buku_table WHERE judul LIKE '%' || :query || '%' COLLATE NOCASE")
    suspend fun searchBukuByJudul(query: String): List<Buku>

    // Get a book by its ID
    @Query("SELECT * FROM buku_table WHERE id = :id LIMIT 1")
    suspend fun getBukuById(id: Int): Buku?

    @Query("SELECT * FROM buku_table WHERE id = :id LIMIT 1")
    fun getBukuByIdPinjam(id: Int): LiveData<Buku?>
    // Get all books ordered by title (synchronous)
    @Query("SELECT * FROM buku_table ORDER BY judul ASC")
    suspend fun getAllBukuSync(): List<Buku> // Added synchronous function



    @Query("SELECT * FROM buku_table WHERE judul = :judul")
    fun getBukuByJudul(judul: String): LiveData<Buku?>

    @Query("SELECT * FROM buku_table")
    fun getAllDataLive(): LiveData<List<Buku>>

    @Query("SELECT id FROM buku_table")
    suspend fun getAllBukuIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bukuList: List<Buku>)

    @Query("DELETE FROM buku_table")
    suspend fun deleteAllBuku() // Menghapus semua buku

//    @Query("SELECT * FROM buku_table WHERE syncronize = 0")
//    suspend fun getUnsyncedBuku(): List<Buku>
}
