package com.example.perpustakaan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pinjam_table")
data class Pinjam(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val namaUser: String,
    val judulBuku: String,
    val tanggalPinjam: String,
    val tanggalKembali: String,
    var statusKembali: String = "Belum Kembali"
)
