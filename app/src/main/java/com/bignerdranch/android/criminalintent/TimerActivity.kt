package com.bignerdranch.android.criminalintent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.criminalintent.viewmodel.CrimeListViewModel

private const val ARG_CRIME_ID = "crime_id"

private const val TAG = "TimerActivity"
class TimerActivity : AppCompatActivity() {
    private lateinit var timer: Chronometer
    private lateinit var btStop: Button
    private lateinit var btStart: Button
    private lateinit var btCreate: Button

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        supportActionBar?.hide()

        timer = findViewById(R.id.timer)
        btStop = findViewById(R.id.bt_stop_timer)
        btStart = findViewById(R.id.bt_start_timer)
        btCreate = findViewById(R.id.bt_create_note)

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
        btCreate.setOnClickListener{
            val note = Note()
            crimeListViewModel.addCrime(note)

            val timeShow = timer.text.toString().split(":")
//            Log.d(TAG,""+timeShow[0]+" "+timeShow[1])

            val duration = timeShow[0].toInt()
            val intent = Intent(this, CrimeActivity::class.java)

            intent.putExtra(ARG_CRIME_ID,note.id)
            intent.putExtra("duration",duration)
            startActivity(intent)
            this.finish()
        }
    }
}