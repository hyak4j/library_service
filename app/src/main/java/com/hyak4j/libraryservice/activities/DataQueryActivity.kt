package com.hyak4j.libraryservice.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hyak4j.libraryservice.databinding.ActivityDataQueryBinding
import com.hyak4j.libraryservice.db.AppDatabase
import com.hyak4j.libraryservice.util.IsbnUtil
import com.hyak4j.libraryservice.util.UserIdUtil
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

        // 查詢所有已註冊讀者
        btnAllUsers.setOnClickListener {
            singleThreadExecutor.execute {
                val db = AppDatabase.buildAppDatabase(this)
                val userDao = db.getUserDao()
                val bookDao = db.getBookDao()
                var resultText = ""
                for (user in userDao.getAllUsers()) {
                    resultText += "User name is ${user.name}\n"
                    resultText += "User ID is ${user.id}\n"
                    resultText += "User is now borrowing: \n"
                    var isBorrowingBooks = false
                    val bookBorrowing = arrayOf(user.bookBorrowing1, user.bookBorrowing2, user.bookBorrowing3,
                                                user.bookBorrowing4, user.bookBorrowing5)
                    for (i in bookBorrowing.indices) {
                        // 確認讀者有無借閱中的書籍，每人最多5本
                        if (bookBorrowing[i] != null) {
                            isBorrowingBooks = true
                            val bookFound = bookDao.getBookByISBN(bookBorrowing[i]!!)
                            resultText += "${bookFound.isbn} ${bookFound.bookName}\n"
                        }
                    }
                    if (!isBorrowingBooks) {
                        resultText += "none \n"
                    }
                    resultText += "================\n"
                }
                runOnUiThread {
                    txtResult.text = resultText
                }
            }
        }

        // 查詢所有已註冊書籍
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

        // 以ISBN查詢書籍或以ID查詢讀者
        btnSearch.setOnClickListener {
            val id = dataId.text.toString()
            if (id == "") {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("ID Error")
                alertDialog.setMessage("ID cannot be empty.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return@setOnClickListener
            }

            if (id[0].isLetter() && !UserIdUtil.checkUserID(id, this)) {
                // 字母開頭，但不符合讀者ID規則
                return@setOnClickListener
            } else if (!id[0].isLetter() && !IsbnUtil.checkISBN(id, this)) {
                // 非字母開頭，但不符合書籍ISBN規則
                return@setOnClickListener
            }

            singleThreadExecutor.execute {
                try {
                    val db = AppDatabase.buildAppDatabase(this)
                    val userDao = db.getUserDao()
                    val bookDao = db.getBookDao()
                    var resultText = ""
                    if (id[0].isLetter()) {
                        // 以ID查詢讀者
                        val user = userDao.getUserById(id)
                        if (user != null) {
                            resultText += "UserName is ${user.name}\n"
                            resultText += "UserID is ${user.id}\n"
                            resultText += "The borrowing books of this user are:\n"

                            var isBorrowingBooks = false
                            val bookBorrowing = arrayOf(user.bookBorrowing1, user.bookBorrowing2, user.bookBorrowing3,
                                user.bookBorrowing4, user.bookBorrowing5)
                            for (i in bookBorrowing.indices) {
                                // 確認讀者有無借閱中的書籍，每人最多5本
                                if (bookBorrowing[i] != null) {
                                    isBorrowingBooks = true
                                    val bookFound = bookDao.getBookByISBN(bookBorrowing[i]!!)
                                    resultText += "${bookFound.isbn} ${bookFound.bookName}\n"
                                }
                            }
                            if (!isBorrowingBooks) {
                                resultText += "none \n"
                            }
                        } else {
                            resultText += "User not found"
                        }
                    } else {
                        // 以ISBN查詢書籍
                        val book = bookDao.getBookByISBN(id)
                        resultText += if (book != null) {
                            "${book.isbn} ${book.bookName}"
                        } else {
                            "Book not found"
                        }
                    }

                    runOnUiThread {
                        txtResult.text = resultText
                    }
                } catch (e: Exception) {
                    txtResult.text = e.message
                }
            }
        }
    }
}