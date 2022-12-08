package com.bignerdranch.android.timelog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.timelog.CrimeRepository
import com.bignerdranch.android.timelog.bean.Note
import java.util.*

class NoteListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    private val crimeLiveData = CrimeRepository.get()
    val crimeListLiveData = crimeLiveData.getCrimes()

    fun addCrime(note : Note){
        crimeRepository.addCrime(note)
    }
    fun deleteNote(note : Note) {
        crimeRepository.deleteCrime(note)
    }

    fun findCrime(str: String) : LiveData<List<Note>> {
        return crimeRepository.findCrime(str)
    }
    fun getAllDates() : LiveData<List<Date>> {
        return crimeRepository.getAllDates()
    }
}