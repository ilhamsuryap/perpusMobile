package com.example.perpustakaan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.Dao.DaoBuku
import com.example.perpustakaan.Dao.DaoPinjam
import com.example.perpustakaan.Dao.DaoUser
import com.example.perpustakaan.entity.Pinjam
import com.example.perpustakaan.entity.User

@Database(entities = [Buku::class, Pinjam::class, User::class], version = 4, exportSchema = false)
abstract class PerpustakaanDatabase : RoomDatabase() {

    abstract fun daobuku(): DaoBuku
    abstract fun daopinjam(): DaoPinjam
    abstract fun userDAO(): DaoUser

    companion object {
        @Volatile
        private var INSTANCE: PerpustakaanDatabase? = null

        fun getDatabase(context: Context): PerpustakaanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PerpustakaanDatabase::class.java,
                    "perpus_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
