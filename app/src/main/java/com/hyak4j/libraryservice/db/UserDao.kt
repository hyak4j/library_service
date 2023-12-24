package com.hyak4j.libraryservice.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM User WHERE id=:id")
    fun getUserById(id: String): User

    @Insert
    fun insertOneNewUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteOneUser(user: User)
}