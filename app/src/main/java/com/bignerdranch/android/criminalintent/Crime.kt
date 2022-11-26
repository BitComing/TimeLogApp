package com.bignerdranch.android.criminalintent

import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "CRIME")
data class Crime (@PrimaryKey
                  val id: UUID = UUID.randomUUID(),
                  var title: String = "",
                  var date: Date = Date(),
                  var duration: Int = 0,
                  var isSolved: Boolean = false,
                  var suspect: String = "") {
    val photoFileName get() = "IMG_$id.jpg"
}

