package com.bignerdranch.android.criminalintent.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "TODOs")
data class Todo (
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var isSolved: Boolean = false,
)