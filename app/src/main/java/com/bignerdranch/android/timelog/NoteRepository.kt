package com.bignerdranch.android.timelog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.timelog.bean.Note
import com.bignerdranch.android.timelog.bean.Todo
import com.bignerdranch.android.timelog.database.*
//import com.bignerdranch.android.timelog.database.migration_3_4
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

class NoteRepository private constructor(context: Context){
    private val database : NoteDatabase = Room.databaseBuilder(
        context.applicationContext,
        NoteDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(migration_1_2)
        .addMigrations(migration_2_3)
        .addMigrations(migration_3_4)
        .addMigrations(migration_4_5)
        .build()

    private val crimeDao = database.noteDao()
    private val todoDao = database.todoDao()

    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getCrimes(): LiveData<List<Note>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Note?> = crimeDao.getCrime(id)

    fun findCrime(str: String): LiveData<List<Note>> = crimeDao.findCrime(str)
    fun getAllDates() : LiveData<List<Date>> = crimeDao.getAllDates()
    fun updateCrime(note: Note) {
        executor.execute {
            crimeDao.updateNote(note)
        }
    }
    fun addCrime(note: Note) {
        executor.execute {
            crimeDao.addNote(note)
        }
    }
    fun deleteCrime(note: Note) {
        executor.execute {
            crimeDao.deleteNote(note)
        }
    }

    // 下方是todo的操作
    fun getTodos(): LiveData<List<Todo>> = todoDao.getTodos()
    fun getTodo(id: UUID): LiveData<Todo?> = todoDao.getTodo(id)
    fun addTodo(todo: Todo) {
        executor.execute {
            todoDao.addTodo(todo)
        }
    }
    fun deleteTodo(todo: Todo) {
        executor.execute {
            todoDao.deleteTodo(todo)
        }
    }
    fun deleteTodoId(tid: String) {
        executor.execute {
            todoDao.deleteTodoId(tid)
        }
    }


//    fun getPhotoFile(crime: Crime): File = File(filesDir, crime.photoFileName)

    companion object {
        private var INSTANCE: NoteRepository?=null
        fun initialize(context: Context) {
            if(INSTANCE == null) {
                // 构造CrimeRepository的方法
                INSTANCE = NoteRepository(context)
            }
        }
        fun get(): NoteRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }

    }
}