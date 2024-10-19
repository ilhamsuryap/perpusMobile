package com.example.perpustakaan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.perpustakaan.Dao.DaoBuku
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Dao.DaoPinjam
import com.example.perpustakaan.entity.Pinjam
import com.example.perpustakaan.user.user
import com.example.perpustakaan.user.userDAO

@Database(entities = [Buku::class, Pinjam::class, user::class], version = 3, exportSchema = false)
abstract class PerpustakaanDatabase : RoomDatabase() {
    abstract fun daobuku(): DaoBuku
    abstract fun daopinjam(): DaoPinjam
    abstract fun userDAO(): userDAO


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