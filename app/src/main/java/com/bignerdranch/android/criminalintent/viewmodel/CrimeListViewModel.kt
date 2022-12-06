package com.bignerdranch.android.criminalintent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.criminalintent.CrimeRepository
import com.bignerdranch.android.criminalintent.bean.Note
import java.util.*

class CrimeListViewModel : ViewModel() {

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