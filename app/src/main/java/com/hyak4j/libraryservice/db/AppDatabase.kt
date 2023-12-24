package com.hyak4j.libraryservice.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Book::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    companion object{
        private var db: AppDatabase? = null
        fun buildAppDatabase(context: Context): AppDatabase{
            // 建置Database耗資源，採用Singleton Pattern
            if (db == null){
                db = Room.databaseBuilder(context, AppDatabase::class.java, "library.db").build()
            }
            return db!!
        }
    }
    abstract fun getUserDao(): UserDao
    abstract fun getBookDao(): BookDao
}