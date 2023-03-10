package com.bignerdranch.android.timelog.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "CRIME")

data class Note (@PrimaryKey
                 val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var duration: Int = 0,
                 var hour: Int = 0,
                 var min: Int = 0,
                 var isSolved: Boolean = false,
                 var suspect: String = "")

{
    val photoFileName get() = "IMG_$id.jpg"
}

