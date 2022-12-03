package com.bignerdranch.android.criminalintent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.criminalintent.CrimeRepository
import com.bignerdranch.android.criminalintent.Note
import java.util.*

class CrimeDetailViewModel(): ViewModel() {
    private val crimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>()


    var noteLiveData: LiveData<Note?> =
        Transformations.switchMap(crimeIdLiveData) {
            crimeId -> crimeRepository.getCrime(crimeId)
        }

    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }

    fun saveCrime(note: Note) {
        crimeRepository.updateCrime(note)
    }
    fun deleteCrime(note: Note) {
        crimeRepository.deleteCrime(note)
    }

//    fun getPhotoFile(crime: Crime): File {
//        return crimeRepository.getPhotoFile(crime)
//    }
}
