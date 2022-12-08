package com.bignerdranch.android.timelog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.timelog.NoteRepository
import com.bignerdranch.android.timelog.bean.Note
import java.util.*

class NoteDetailViewModel(): ViewModel() {
    private val noteRepository = NoteRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>()


    var noteLiveData: LiveData<Note?> =
        Transformations.switchMap(crimeIdLiveData) {
            crimeId -> noteRepository.getCrime(crimeId)
        }

    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }

    fun saveCrime(note: Note) {
        noteRepository.updateCrime(note)
    }
    fun deleteCrime(note: Note) {
        noteRepository.deleteCrime(note)
    }

//    fun getPhotoFile(crime: Crime): File {
//        return crimeRepository.getPhotoFile(crime)
//    }
}

