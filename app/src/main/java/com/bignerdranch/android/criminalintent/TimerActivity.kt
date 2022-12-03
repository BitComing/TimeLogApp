package com.bignerdranch.android.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Chronometer

class TimerActivity : AppCompatActivity() {
    private lateinit var timer: Chronometer
    private lateinit var btStop: Button
    private lateinit var btStart: Button
    private lateinit var btRestart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        supportActionBar?.hide()

        timer = findViewById(R.id.timer)
        btStop = findViewById(R.id.bt_stop_timer)
        btStart = findViewById(R.id.bt_start_timer)
        btRestart = findViewById(R.id.bt_restart_timer)

    }

    override fun onStart() {
        super.onStart()
        timer.start()
        btStop.setOnClickListener{
            timer.stop()
        }
        btStart.setOnClickListener{
            timer.start()
        }
    }
}