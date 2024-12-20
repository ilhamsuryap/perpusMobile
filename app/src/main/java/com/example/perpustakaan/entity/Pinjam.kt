package com.example.perpustakaan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.PropertyName

@Entity(tableName = "pinjam_table")
data class Pinjam(
    @PrimaryKey(autoGenerate = true)
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id_pinjam: Int = 0,

    @get:PropertyName("namaanggota")
    val namaanggota: String = "",

    @get:PropertyName("judulbuku_pinjam")
    val judulbuku_pinjam: String = "",

    @get:PropertyName("tanggalpinjam")
    val tanggalpinjam: String = "",

    @get:PropertyName("tanggalkembali")
    val tanggalkembali: String = "",

//    val syncronize: Boolean = false
)
