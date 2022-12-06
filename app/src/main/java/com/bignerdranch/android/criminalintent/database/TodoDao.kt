package com.bignerdranch.android.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.criminalintent.bean.Todo
import java.util.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todos WHERE tid=(:tid)")
    fun getTodo(tid: UUID): LiveData<Todo?>

//    @Query("SELECT * FROM todos WHERE title like '%'||:str||'%'")
//    fun findCrime(str: String): LiveData<List<Note>>

    @Update
    fun updateTodo(note: Todo)

    @Insert
    fun addTodo(note: Todo)

    @Delete
    fun deleteTodo(note: Todo)

    @Query("Delete From todos where tid =(:tid)")
    fun deleteTodoId(tid: String)

//    @Query("SELECT DISTINCT date FROM crime order by date desc")
//    fun getAllDates(): LiveData<List<Date>>

//    @Query("SELECT DISTINCT date FROM crime order by date desc")
//    fun getAllDates(): LiveData<List<Date>>
}