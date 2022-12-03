package com.bignerdranch.android.criminalintent

import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.time.Duration.Companion.hours

@Entity(tableName = "CRIME")
data class Note (@PrimaryKey
                  val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var duration: Int = 0,
                 var hour: Int = 0,
                 var min: Int = 0,
                 var isSolved: Boolean = false,
                 var suspect: String = "") {
    val photoFileName get() = "IMG_$id.jpg"
}

