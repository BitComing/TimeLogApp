package com.bignerdranch.android.timelog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.timelog.NoteRepository
import com.bignerdranch.android.timelog.bean.Todo
import java.util.*

class TodoListViewModel : ViewModel() {

    private val noteRepository = NoteRepository.get()
    private val crimeLiveData = NoteRepository.get()
    val noteListLiveData = crimeLiveData.getTodos()

    fun addTodo(todo : Todo){
        noteRepository.addTodo(todo)
    }
    fun deleteTodo(todo : Todo) {
        noteRepository.deleteTodo(todo)
    }
    fun deleteTodoId(tid: String) {
        noteRepository.deleteTodoId(tid)
    }

    fun getAllDates() : LiveData<List<Date>> {
        return noteRepository.getAllDates()
    }

//    fun findCrime(str: String) : LiveData<List<Note>> {
//        return crimeRepository.findCrime(str)
//    }
}