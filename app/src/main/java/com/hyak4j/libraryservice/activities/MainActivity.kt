package com.hyak4j.libraryservice.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hyak4j.libraryservice.R
import com.hyak4j.libraryservice.db.AppDatabase
import com.hyak4j.libraryservice.db.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDatabase.buildAppDatabase(this)
        val userDao = db.getUserDao()
        Thread{
            // 測試用
            userDao.insertOneNewUser(User("1", "Henry", null, null, null, null, null))
            println(userDao.getAllUsers())
        }.start()
    }
}