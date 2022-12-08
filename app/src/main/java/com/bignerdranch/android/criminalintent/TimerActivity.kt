package com.bignerdranch.android.criminalintent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Chronometer
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.criminalintent.bean.Note
import com.bignerdranch.android.criminalintent.viewmodel.NoteListViewModel
import com.bignerdranch.android.criminalintent.viewmodel.TodoListViewModel

private const val ARG_CRIME_ID = "crime_id"

private const val TAG = "TimerActivity"
class TimerActivity : AppCompatActivity() {
    private lateinit var timer: Chronometer
    private lateinit var btStop: Button
    private lateinit var btStart: Button
    private lateinit var btCreate: Button

    private var tmpContent: String? = null
    private var tmpId: String? = null

    private val noteListViewModel: NoteListViewModel by lazy {
        ViewModelProvider(this).get(NoteListViewModel::class.java)
    }
    private val todoListViewModel: TodoListViewModel by lazy {
        ViewModelProvider(this).get(TodoListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        supportActionBar?.hide()

        timer = findViewById(R.id.timer)
        btStop = findViewById(R.id.bt_stop_timer)
        btStart = findViewById(R.id.bt_start_timer)
        btCreate = findViewById(R.id.bt_create_note)

        val bundle = this.getIntent().getExtras()
        tmpContent = bundle?.getSerializable("todo_content") as String?
        tmpId = bundle?.getSerializable("todo_id") as String?
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
            noteListViewModel.addCrime(note)

            tmpId?.let { it1 -> todoListViewModel.deleteTodoId(it1) }

            val timeShow = timer.text.toString().split(":")
            var duration = 0
            when(timeShow.size) {
                1 -> duration = 0;
                2-> duration = timeShow[0].toInt()
                3-> duration = timeShow[0].toInt()*60 + timeShow[1].toInt()
            }
//            Log.d(TAG,""+timeShow[0]+" "+timeShow[1])


            Intent(this, CrimeActivity::class.java).let {
               if (tmpContent != null) {
                   it.putExtra("todo_content",tmpContent)
               }
               it.putExtra(ARG_CRIME_ID,note.id)
               it.putExtra("duration",duration)
               startActivity(it)
            }

            this.finish()
        }
    }
}