package com.bignerdranch.android.timelog

import android.app.Application

class TimeLogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}