package com.bignerdranch.android.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Chronometer

class TimerActivity : AppCompatActivity() {
    private lateinit var timer: Chronometer
    private lateinit var btStop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        supportActionBar?.hide()

        timer = findViewById(R.id.timer)
        btStop = findViewById(R.id.bt_stop_timer)
    }

    override fun onStart() {
        super.onStart()
        timer.start()
        btStop.setOnClickListener{
            timer.stop()
        }
    }
}