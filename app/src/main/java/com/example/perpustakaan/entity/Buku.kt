package com.example.perpustakaan.Dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buku_table")
data class Buku(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val gambarUrl: String? = null,
    val judul: String,
    val penulis: String,
    val tahunTerbit: Int,
    val stok: Int,
    val deskripsi: String
)

