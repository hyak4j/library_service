package com.hyak4j.libraryservice.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("barrowTo"),
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Book(
    @PrimaryKey val isbn: String,
    val bookName: String,
    val barrowTo: String?
)