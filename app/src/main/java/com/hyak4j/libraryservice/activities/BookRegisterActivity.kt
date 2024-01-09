package com.hyak4j.libraryservice.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hyak4j.libraryservice.databinding.ActivityBookRegisterBinding
import com.hyak4j.libraryservice.db.AppDatabase
import com.hyak4j.libraryservice.db.Book
import java.util.concurrent.Executors

class BookRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookRegisterBinding
    private lateinit var edtBookName: EditText
    private lateinit var edtBookISBN: EditText
    private lateinit var btnBookRegister: Button
    private lateinit var txtBookRegisterResult: TextView
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        edtBookName = binding.edtBookName
        edtBookISBN = binding.edtBookISBN
        btnBookRegister = binding.btnRegisterBook
        txtBookRegisterResult = binding.txtRegisterResult

        btnBookRegister.setOnClickListener {
            /*
                書籍註冊 SUBMIT
                    驗證
                    => Book Name 不可為空
                    => ISBN 不可為空
                    => ISBN 長度10
                    => ISBN validation

                    儲存資料
             */
            if (edtBookName.text.toString() == "") {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Book Title Error")
                alertDialog.setMessage("The book title cannot be empty.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return@setOnClickListener
            }

            if (edtBookISBN.text.toString() == "") {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("ISBN Error")
                alertDialog.setMessage("The book ISBN cannot be empty.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return@setOnClickListener
            }

            if (edtBookISBN.text.length != 10) {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("ISBN Error")
                alertDialog.setMessage("The book ISBN length must be 10 digits long.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return@setOnClickListener
            }

            if (!isbnValidation(edtBookISBN.text.toString())) {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("ISBN Error")
                alertDialog.setMessage("The book ISBN is not valid.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return@setOnClickListener
            }

            singleThreadExecutor.execute {
                // 通過檢查 => 儲存書籍資料
                try {
                    val bookName = edtBookName.text.toString()
                    val bookISBN = edtBookISBN.text.toString()
                    val db = AppDatabase.buildAppDatabase(this)
                    db.getBookDao().insertOneNewBook(Book(bookISBN, bookName, null))
                    runOnUiThread {
                        txtBookRegisterResult.text = "The result is:\n The book is registered."
                        edtBookName.setText("")
                        edtBookISBN.setText("")
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        txtBookRegisterResult.text = "The result is:\n${e.message}"
                    }
                }
            }
        }
    }

    private fun isbnValidation(isbn: String): Boolean {
        // 驗證isbn是否正確
        var sum = 0
        for (i in 0 until isbn.length - 1) {
            // 將各項char轉int * 位於第幾碼後加總 (不含第10碼檢查碼)
            sum += (isbn[i] - '0') * (i + 1)
        }
        val checkDigit = sum % 11
        // 最後一碼為檢查碼
        val checkDigitProvided = isbn[isbn.length - 1]
        val checkDigitProvidedToInt = if (checkDigitProvided == 'X') 10
        else {
            checkDigitProvided - '0'
        }
        return checkDigit == checkDigitProvidedToInt
    }
}