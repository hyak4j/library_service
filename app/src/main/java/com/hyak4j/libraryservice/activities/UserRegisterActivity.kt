package com.hyak4j.libraryservice.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hyak4j.libraryservice.databinding.ActivityUserRegisterBinding
import com.hyak4j.libraryservice.db.AppDatabase
import com.hyak4j.libraryservice.db.User
import com.hyak4j.libraryservice.util.UserIdUtil
import java.util.concurrent.Executors

class UserRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserRegisterBinding
    private lateinit var userName: EditText
    private lateinit var userID: EditText
    private lateinit var btnRegisterUser: Button
    private lateinit var txtResult: TextView
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userName = binding.edtUserName
        userID = binding.edtUserId
        btnRegisterUser = binding.btnRegisterUser
        txtResult = binding.txtUserRegisterResult

        btnRegisterUser.setOnClickListener {
            val inputUserName = userName.text.toString()
            val inputUserID = userID.text.toString()
            if (inputUserName == "") {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("UserName Error")
                alertDialog.setMessage("Username cannot be empty.")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                alertDialog.create().show()
                return@setOnClickListener
            }

            if (!UserIdUtil.checkUserID(inputUserID, this)) {
                return@setOnClickListener
            }

            singleThreadExecutor.execute {
                try {
                    val db = AppDatabase.buildAppDatabase(this)
                    val userDao = db.getUserDao()
                    userDao.insertOneNewUser(User(inputUserID, inputUserName,
                        null, null, null, null, null))
                    runOnUiThread {
                        txtResult.text = "The result is:\n The user is registered."
                        userName.setText("")
                        userID.setText("")
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        txtResult.text = "The result is:\n${e.message}"
                    }
                }

            }
        }
    }
}