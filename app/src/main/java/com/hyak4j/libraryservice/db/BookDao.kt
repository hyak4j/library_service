package com.hyak4j.libraryservice.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookDao {

    @Query("SELECT * FROM Book")
    fun getAllBooks(): List<Book>

    @Query("SELECT * FROM Book WHERE isbn=:isbn")
    fun getBookByISBN(isbn: String): Book

    @Insert
    fun insertOneNewBook(book: Book)

    @Update
    fun updateOneBook(book: Book)

    @Delete
    fun deleteOneBook(book: Book)
}