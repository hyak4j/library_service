package com.hyak4j.libraryservice.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hyak4j.libraryservice.databinding.ActivityBorrowReturnBinding
import com.hyak4j.libraryservice.db.AppDatabase
import com.hyak4j.libraryservice.db.Book
import com.hyak4j.libraryservice.db.User
import com.hyak4j.libraryservice.util.IsbnUtil
import com.hyak4j.libraryservice.util.UserIdUtil
import java.util.concurrent.Executors

class BorrowReturnActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBorrowReturnBinding
    private lateinit var edtISBN: EditText
    private lateinit var edtUserID: EditText
    private lateinit var btnBorrow: Button
    private lateinit var btnReturn: Button
    private lateinit var txtResult: TextView
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBorrowReturnBinding.inflate(layoutInflater)
        setContentView(binding.root)
        edtISBN = binding.edtBookISBNBr
        edtUserID = binding.edtUserIdBr
        btnBorrow = binding.btnBorrow
        btnReturn = binding.btnReturn
        txtResult = binding.txtBorrowReturnResult

        btnBorrow.setOnClickListener {
            val isbn = edtISBN.text.toString()
            val userId = edtUserID.text.toString()
            if (!IsbnUtil.checkISBN(isbn, this)) {
                return@setOnClickListener
            }
            if (!UserIdUtil.checkUserID(userId, this)) {
                return@setOnClickListener
            }

            singleThreadExecutor.execute {
                try {
                    // 找讀者
                    val db = AppDatabase.buildAppDatabase(this)
                    val userDao = db.getUserDao()
                    val user = userDao.getUserById(userId)
                    if (user == null) {
                        //查無讀者
                        runOnUiThread {
                            txtResult.text = "Borrow failed. User not found"
                        }
                        return@execute
                    }

                    val bookDao = db.getBookDao()
                    val book = bookDao.getBookByISBN(isbn)
                    if (book == null) {
                        //查無書籍
                        runOnUiThread {
                            txtResult.text = "Borrow failed. Book not found"
                        }
                        return@execute
                    }

                    if (user.bookBorrowing1 != null &&
                        user.bookBorrowing2 != null &&
                        user.bookBorrowing3 != null &&
                        user.bookBorrowing4 != null &&
                        user.bookBorrowing5 != null
                    ) {
                        //已借閱5本書
                        runOnUiThread {
                            txtResult.text = "Borrow failed. Cannot borrow more than 5 books"
                        }
                        return@execute
                    }

                    if (book.barrowTo != null) {
                        // 書籍已借出
                        runOnUiThread {
                            txtResult.text = "Borrow failed. This book (${isbn}) has been borrowed"
                        }
                        return@execute
                    }

                    if (user.bookBorrowing2 == null) {
                        userDao.updateUser(
                            User(
                                user.id,
                                user.name,
                                user.bookBorrowing1,
                                isbn,
                                user.bookBorrowing3,
                                user.bookBorrowing4,
                                user.bookBorrowing5
                            )
                        )
                    }
                    if (user.bookBorrowing3 == null) {
                        userDao.updateUser(
                            User(
                                user.id,
                                user.name,
                                user.bookBorrowing1,
                                user.bookBorrowing2,
                                isbn,
                                user.bookBorrowing4,
                                user.bookBorrowing5
                            )
                        )
                    }
                    if (user.bookBorrowing4 == null) {
                        userDao.updateUser(
                            User(
                                user.id,
                                user.name,
                                user.bookBorrowing1,
                                user.bookBorrowing2,
                                user.bookBorrowing3,
                                isbn,
                                user.bookBorrowing5
                            )
                        )
                    }
                    if (user.bookBorrowing5 == null) {
                        userDao.updateUser(
                            User(
                                user.id,
                                user.name,
                                user.bookBorrowing1,
                                user.bookBorrowing2,
                                user.bookBorrowing3,
                                user.bookBorrowing4,
                                isbn
                            )
                        )
                    }
                    if (user.bookBorrowing1 == null) {
                        userDao.updateUser(
                            User(
                                user.id,
                                user.name,
                                isbn,
                                user.bookBorrowing2,
                                user.bookBorrowing3,
                                user.bookBorrowing4,
                                user.bookBorrowing5
                            )
                        )
                    }
                    bookDao.updateOneBook(Book(book.isbn, book.bookName, user.id))
                    runOnUiThread {
                        txtResult.text = "Borrow Success!"
                        edtISBN.setText("")
                        edtUserID.setText("")
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        txtResult.text = e.message
                    }
                }
            }
        }
    }
}