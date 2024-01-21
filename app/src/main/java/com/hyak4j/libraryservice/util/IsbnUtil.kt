package com.hyak4j.libraryservice.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

class IsbnUtil {
    companion object {
        fun checkISBN(bookISBN: String, context: Context): Boolean {
            if (bookISBN == "") {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("ISBN Error")
                alertDialog.setMessage("The book ISBN cannot be empty.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return false
            }

            if (bookISBN.length != 10) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("ISBN Error")
                alertDialog.setMessage("The book ISBN length must be 10 digits long.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return false
            }

            if (!isbnValidation(bookISBN)) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("ISBN Error")
                alertDialog.setMessage("The book ISBN is not valid.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return false
            }
            return true
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
}