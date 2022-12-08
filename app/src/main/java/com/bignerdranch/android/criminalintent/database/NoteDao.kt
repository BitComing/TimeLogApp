package com.bignerdranch.android.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.criminalintent.bean.Note
import java.util.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM crime order by date desc")
    fun getCrimes(): LiveData<List<Note>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Note?>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getNote(id: UUID): Note?

    @Query("SELECT * FROM crime WHERE title like '%'||:str||'%'")
    fun findCrime(str: String): LiveData<List<Note>>

    @Update
    fun updateCrime(note: Note)

    @Insert
    fun addCrime(note: Note)

    @Delete
    fun deleteCrime(note: Note)

//    @Query("SELECT DISTINCT date FROM crime order by date desc")
//    fun getAllDates(): LiveData<List<Date>>

    @Query("SELECT DISTINCT date FROM crime order by date desc")
    fun getAllDates(): LiveData<List<Date>>
}

