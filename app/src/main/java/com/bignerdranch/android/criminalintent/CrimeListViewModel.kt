package com.bignerdranch.android.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    private val crimeLiveData = CrimeRepository.get()
    val crimeListLiveData = crimeLiveData.getCrimes()

    fun addCrime(crime :Crime){
        crimeRepository.addCrime(crime)
    }

    fun findCrime(str: String) : LiveData<List<Crime>> {
        return crimeRepository.findCrime(str)
    }
}