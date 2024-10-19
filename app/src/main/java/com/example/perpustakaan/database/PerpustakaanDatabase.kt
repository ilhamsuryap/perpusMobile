package com.example.perpustakaan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.perpustakaan.Dao.DaoBuku
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Dao.DaoPinjam
import com.example.perpustakaan.entity.Pinjam

@Database(entities = [Buku::class, Pinjam::class], version = 2, exportSchema = false)
abstract class PerpustakaanDatabase : RoomDatabase() {
    abstract fun daobuku(): DaoBuku
    abstract fun daopinjam(): DaoPinjam

    companion object{
        @Volatile
        private var Instance: PerpustakaanDatabase? = null

        fun getDatabase(context: Context): PerpustakaanDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PerpustakaanDatabase::class.java, "perpustakaan_db")
                    .fallbackToDestructiveMigrationFrom()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}