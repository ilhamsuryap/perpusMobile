package com.example.perpustakaan.user

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [user::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): userDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    // Tambahkan strategi fallback migration jika diperlukan
                    .fallbackToDestructiveMigration() // Optional: untuk menghancurkan dan membangun kembali database saat skema berubah
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
