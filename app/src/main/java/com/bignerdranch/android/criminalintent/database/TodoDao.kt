package com.bignerdranch.android.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.criminalintent.Note
import com.bignerdranch.android.criminalintent.bean.Todo
import java.util.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todos WHERE id=(:id)")
    fun getTodo(id: UUID): LiveData<Note?>

//    @Query("SELECT * FROM todos WHERE title like '%'||:str||'%'")
//    fun findCrime(str: String): LiveData<List<Note>>

    @Update
    fun updateTodo(note: Todo)

    @Insert
    fun addTodo(note: Todo)

    @Delete
    fun deleteTodo(note: Todo)

//    @Query("SELECT DISTINCT date FROM crime order by date desc")
//    fun getAllDates(): LiveData<List<Date>>

//    @Query("SELECT DISTINCT date FROM crime order by date desc")
//    fun getAllDates(): LiveData<List<Date>>
}