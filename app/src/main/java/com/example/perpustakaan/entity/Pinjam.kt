package com.example.perpustakaan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pinjam_table")
data class Pinjam(
    @PrimaryKey(autoGenerate = true) val id_pinjam: Int = 0,
    @ColumnInfo(name = "Nama_Anggota") val namaanggota: String,
    @ColumnInfo(name = "Judul_Buku") val judulbuku_pinjam: String,
    @ColumnInfo(name = "Tanggal_Pinjam") val tanggalpinjam: String,
    @ColumnInfo(name = "Tanggal_Kembali") val tanggalkembali: String,
//    @ColumnInfo(name = "Status") val status: String
)
