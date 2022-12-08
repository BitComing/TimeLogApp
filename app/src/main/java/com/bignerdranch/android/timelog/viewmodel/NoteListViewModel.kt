package com.bignerdranch.android.timelog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.timelog.NoteRepository
import com.bignerdranch.android.timelog.bean.Note
import java.util.*

class NoteListViewModel : ViewModel() {

    private val noteRepository = NoteRepository.get()
    private val crimeLiveData = NoteRepository.get()
    val crimeListLiveData = crimeLiveData.getCrimes()

    fun addCrime(note : Note){
        noteRepository.addCrime(note)
    }
    fun deleteNote(note : Note) {
        noteRepository.deleteCrime(note)
    }

    fun findCrime(str: String) : LiveData<List<Note>> {
        return noteRepository.findCrime(str)
    }
    fun getAllDates() : LiveData<List<Date>> {
        return noteRepository.getAllDates()
    }
}