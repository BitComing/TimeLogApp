package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.criminalintent.bean.Todo
import com.bignerdranch.android.criminalintent.database.CrimeDatabase
import com.bignerdranch.android.criminalintent.database.migration_1_2
import com.bignerdranch.android.criminalintent.database.migration_2_3
//import com.bignerdranch.android.criminalintent.database.migration_3_4
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"
//通过上下文创建一个类？
class CrimeRepository private constructor(context: Context){
    private val database : CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(migration_1_2)
        .addMigrations(migration_2_3)
//        .addMigrations(migration_3_4)
        .build()

    private val crimeDao = database.crimeDao()
//    private val todoDao = database.todoDao()

    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getCrimes(): LiveData<List<Note>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Note?> = crimeDao.getCrime(id)

    fun findCrime(str: String): LiveData<List<Note>> = crimeDao.findCrime(str)
    fun getAllDates() : LiveData<List<Date>> = crimeDao.getAllDates()
    fun updateCrime(note: Note) {
        executor.execute {
            crimeDao.updateCrime(note)
        }
    }
    fun addCrime(note: Note) {
        executor.execute {
            crimeDao.addCrime(note)
        }
    }
    fun deleteCrime(note: Note) {
        executor.execute {
            crimeDao.deleteCrime(note)
        }
    }

    // 下方是todo的操作
//    fun getTodos(): LiveData<List<Todo>> = todoDao.getTodos()
//    fun getTodo(id: UUID): LiveData<Todo?> = todoDao.getTodo(id)
//    fun addTodo(todo: Todo) {
//        executor.execute {
//            todoDao.addTodo(todo)
//        }
//    }
//    fun deleteTodo(todo: Todo) {
//        executor.execute {
//            todoDao.deleteTodo(todo)
//        }
//    }


//    fun getPhotoFile(crime: Crime): File = File(filesDir, crime.photoFileName)

    companion object {
        private var INSTANCE: CrimeRepository?=null
        fun initialize(context: Context) {
            if(INSTANCE == null) {
                // 构造CrimeRepository的方法
                INSTANCE = CrimeRepository(context)
            }
        }
        fun get(): CrimeRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }

    }
}