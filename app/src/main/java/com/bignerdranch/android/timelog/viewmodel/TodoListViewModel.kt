package com.bignerdranch.android.timelog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.timelog.CrimeRepository
import com.bignerdranch.android.timelog.bean.Todo
import java.util.*

class TodoListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    private val crimeLiveData = CrimeRepository.get()
    val noteListLiveData = crimeLiveData.getTodos()

    fun addTodo(todo : Todo){
        crimeRepository.addTodo(todo)
    }
    fun deleteTodo(todo : Todo) {
        crimeRepository.deleteTodo(todo)
    }
    fun deleteTodoId(tid: String) {
        crimeRepository.deleteTodoId(tid)
    }

    fun getAllDates() : LiveData<List<Date>> {
        return crimeRepository.getAllDates()
    }

//    fun findCrime(str: String) : LiveData<List<Note>> {
//        return crimeRepository.findCrime(str)
//    }
}