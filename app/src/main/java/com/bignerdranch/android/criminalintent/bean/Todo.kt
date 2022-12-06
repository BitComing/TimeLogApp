package com.bignerdranch.android.criminalintent.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "TODOS")

data class Todo (
    @PrimaryKey
    val tid: UUID = UUID.randomUUID(),
    var date: Date = Date(),
    var content: String = "",
    var isSolved: Boolean = false,
)