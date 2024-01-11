package com.hyak4j.libraryservice.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hyak4j.libraryservice.databinding.ActivityDataQueryBinding
import com.hyak4j.libraryservice.db.AppDatabase
import java.util.concurrent.Executors

class DataQueryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataQueryBinding
    private lateinit var dataId: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnAllUsers: Button
    private lateinit var btnAllBooks: Button
    private lateinit var txtResult: TextView
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataQueryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataId = binding.edtDataId
        btnSearch = binding.btnSearch
        btnAllUsers = binding.btnFindUser
        btnAllBooks = binding.btnFindBook
        txtResult = binding.txtDataQueryResult

        btnAllBooks.setOnClickListener {
            singleThreadExecutor.execute {
                val db = AppDatabase.buildAppDatabase(this)
                val bookDao = db.getBookDao()
                var resultText = ""
                for (book in bookDao.getAllBooks()) {
                    resultText += "Book name is ${book.bookName}\n"
                    resultText += "Book ISBN is ${book.isbn}\n"
                    resultText += "Book is borrowed to ${book.barrowTo}\n"
                    resultText += "================\n"
                }
                runOnUiThread {
                    txtResult.text = resultText
                }
            }
        }
    }
}