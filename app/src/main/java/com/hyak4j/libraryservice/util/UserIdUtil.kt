package com.hyak4j.libraryservice.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

class UserIdUtil {
    companion object{
        private val letterValueMap = HashMap<Char, String>()

        init {
            // 第一碼轉換mapping
            letterValueMap['A'] = "10" //台北市
            letterValueMap['B'] = "11" //台中市
            letterValueMap['C'] = "12" //基隆市
            letterValueMap['D'] = "13" //台南市
            letterValueMap['E'] = "14" //高雄市
            letterValueMap['F'] = "15" //新北市
            letterValueMap['G'] = "16" //宜蘭縣
            letterValueMap['H'] = "17" //桃園市
            letterValueMap['I'] = "34" //嘉義市
            letterValueMap['J'] = "18" //新竹縣
            letterValueMap['K'] = "19" //苗栗縣
            letterValueMap['M'] = "21" //南投縣
            letterValueMap['N'] = "22" //彰化縣
            letterValueMap['O'] = "35" //新竹市
            letterValueMap['P'] = "23" //雲林縣
            letterValueMap['Q'] = "24" //嘉義縣
            letterValueMap['T'] = "27" //屏東縣
            letterValueMap['U'] = "28" //花蓮縣
            letterValueMap['V'] = "29" //台東縣
            letterValueMap['W'] = "32" //金門縣
            letterValueMap['X'] = "30" //澎湖縣
            letterValueMap['Z'] = "33" //連江縣
        }
        fun checkUserID(UserID: String, context: Context): Boolean {
            if (UserID == "") {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("UserID Error")
                alertDialog.setMessage("UserID cannot be empty.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return false
            }

            if (UserID.length != 10) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("UserID Error")
                alertDialog.setMessage("UserID length must contain 10 letters.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return false
            }

            if (!idValidationCheck(UserID)) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("UserID Error")
                alertDialog.setMessage("UserID is not valid.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return false
            }
            return true
        }

        private fun idValidationCheck(inputID: String): Boolean{
            /*
            1. 將首位英文字母轉成對應數值
            2. 將身分證字號每一位數乘以各自權重，依序為 1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1
            3. 將相乘之後的數值加總，除以 10，若整除則為合法的身分證字號
             */
            if (!inputID[0].isLetter()){
                // 第一碼不是英文字 => 排除
                return false
            }

            val id = inputID.uppercase()
            // 排序權重
            val valueWeight = arrayOf(1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1)
            var sum = 0
            sum += (letterValueMap[id[0]]!![0] - '0') * valueWeight[0]
            sum += (letterValueMap[id[0]]!![1] - '0') * valueWeight[1]
            for (i in 1 until id.length) {
                sum += (id[i] - '0') * valueWeight[i + 1]
            }

            return sum % 10 == 0
        }
    }

}