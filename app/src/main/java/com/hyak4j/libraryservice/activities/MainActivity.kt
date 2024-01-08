package com.hyak4j.libraryservice.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hyak4j.libraryservice.databinding.ActivityMainBinding
import com.hyak4j.libraryservice.db.AppDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegisterBook.setOnClickListener {
            startActivity(Intent(this, BookRegisterActivity::class.java))
        }

        binding.btnRegisterUser.setOnClickListener {
            startActivity(Intent(this, UserRegisterActivity::class.java))
        }

        binding.btnBorrowReturn.setOnClickListener {
            startActivity(Intent(this, BorrowReturnActivity::class.java))
        }

        binding.btnQueryData.setOnClickListener {
            startActivity(Intent(this, DataQueryActivity::class.java))
        }

        val db = AppDatabase.buildAppDatabase(this)
        val userDao = db.getUserDao()
        Thread{
            // 測試用
//            userDao.insertOneNewUser(User("1", "Henry", null, null, null, null, null))
            println(userDao.getAllUsers())
        }.start()
    }
}