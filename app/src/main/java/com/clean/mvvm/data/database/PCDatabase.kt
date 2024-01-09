package com.clean.mvvm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.clean.mvvm.data.database.dao.FavouriteDao
import com.clean.mvvm.data.database.entities.FavImageEntity
import com.clean.mvvm.utils.Constants.Companion.DATABASE_NAME

@Database(
    entities = [
        FavImageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PCDatabase : RoomDatabase() {
    abstract fun favImageDao(): FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: PCDatabase? = null

        fun getInstance(context: Context): PCDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): PCDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                PCDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}