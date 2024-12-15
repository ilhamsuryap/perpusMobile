package com.example.perpustakaan.Dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.PropertyName

@Entity(tableName = "buku_table")
data class Buku(
    @PrimaryKey(autoGenerate = true)
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: Int = 0,

    // Firebase field for gambarUrl (can be null)
    @get:PropertyName("gambarUrl")
    val gambarUrl: String? = null,

    // Firebase field for judul
    @get:PropertyName("judul")
    val judul: String = "",

    // Firebase field for penulis
    @get:PropertyName("penulis")
    val penulis: String = "",

    // Firebase field for tahunTerbit
    @get:PropertyName("tahunTerbit")
    val tahunTerbit: Int = 0,

    // Firebase field for stok
    @get:PropertyName("stok")
    var stok: Int = 0,

    // Firebase field for deskripsi
    @get:PropertyName("deskripsi")
    val deskripsi: String = "",

//    val syncronize: Boolean = false
)
