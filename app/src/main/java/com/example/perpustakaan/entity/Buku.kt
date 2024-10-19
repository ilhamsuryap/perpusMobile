package com.example.perpustakaan.Dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buku_table")
data class Buku(
    @PrimaryKey(autoGenerate = true) val id_buku: Int = 0,
    @ColumnInfo(name = "judul_buku") val judul: String,
    @ColumnInfo(name = "penulis") val penulis: String,
    @ColumnInfo(name = "tahun_terbit") val tahunTerbit: Int,
    @ColumnInfo(name = "stok") val stok: Int,
    @ColumnInfo(name = "deskripsi") val deskripsi: String
)

