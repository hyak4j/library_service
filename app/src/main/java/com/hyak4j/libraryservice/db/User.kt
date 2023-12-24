package com.hyak4j.libraryservice.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = arrayOf("isbn"),
            childColumns = arrayOf("bookBorrowing1"),
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Book::class,
            parentColumns = arrayOf("isbn"),
            childColumns = arrayOf("bookBorrowing2"),
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Book::class,
            parentColumns = arrayOf("isbn"),
            childColumns = arrayOf("bookBorrowing3"),
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Book::class,
            parentColumns = arrayOf("isbn"),
            childColumns = arrayOf("bookBorrowing4"),
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Book::class,
            parentColumns = arrayOf("isbn"),
            childColumns = arrayOf("bookBorrowing5"),
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class User(
    @PrimaryKey val id: String,
    val name: String,
    val bookBorrowing1: String?,
    val bookBorrowing2: String?,
    val bookBorrowing3: String?,
    val bookBorrowing4: String?,
    val bookBorrowing5: String?,
)