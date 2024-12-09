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

@Database(entities = [Buku::class, Pinjam::class, User::class], version = 2, exportSchema = false)
abstract class PerpustakaanDatabase : RoomDatabase() {

    abstract fun daobuku(): DaoBuku
    abstract fun daopinjam(): DaoPinjam
    abstract fun userDAO(): DaoUser

    companion object {
        @Volatile
        private var INSTANCE: PerpustakaanDatabase? = null

        /**
         * Provides a singleton instance of the PerpustakaanDatabase.
         * @param context The application context.
         * @return The instance of the PerpustakaanDatabase.
         */
        fun getDatabase(context: Context): PerpustakaanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PerpustakaanDatabase::class.java,
                    "perpustakaan_db"
                )
                    .fallbackToDestructiveMigration() // Fallback for destructive migration if no migration script is provided
                    // Optionally, you can define a migration strategy here:
                    // .addMigrations(MIGRATION_1_2) // Example migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
