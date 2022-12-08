package com.bignerdranch.android.timelog

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.timelog.bean.Note
import com.bignerdranch.android.timelog.viewmodel.NoteDetailViewModel
//import com.bnuz.example.criminalintentRoom.CrimeFragment
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "NoteActivity"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
class CrimeActivity : AppCompatActivity() {

//    interface Callbacks {
//        fun onCrimeDeleted(fragment: CrimeFragment)
//    }
//
//    private var callbacks: Callbacks? = null
//    private lateinit var photoFile: File
    private lateinit var note : Note

    private lateinit var txtDate: TextView
    private lateinit var txtEnd : TextView
    private lateinit var txtStart: TextView

    private lateinit var edTxtTitle : EditText
    private lateinit var txtDur : EditText

    private lateinit var btnDelete: Button
    private lateinit var btnBack: Button
    private lateinit var btnAdd15 : Button
    private lateinit var btnAdd30 : Button
    private lateinit var btnAdd60 : Button
    private lateinit var addBtnList : LinearLayout

//    private lateinit var startTime: Date
//    private lateinit var endTime: Date
    private lateinit var todayDate: Date
    private lateinit var strTodayDate: String
    private  var tmpDuration:Int? = 0
    private var tmpContent: String? = null

    private val noteDetailViewModel : NoteDetailViewModel by lazy {
        ViewModelProvider(this).get(NoteDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime)
        supportActionBar?.hide()

        txtDate = findViewById(R.id.txt_date)

        edTxtTitle = findViewById(R.id.crime_title)
        txtDur = findViewById(R.id.edit_duration)
        txtEnd = findViewById(R.id.edit_now_time)
        txtStart = findViewById(R.id.edit_start_time)

        btnDelete = findViewById(R.id.btn_delete)
        btnBack = findViewById(R.id.btn_back)

        btnAdd15 = findViewById(R.id.btn_add_15)
        btnAdd30 = findViewById(R.id.btn_add_30)
        btnAdd60 = findViewById(R.id.btn_add_60)
        addBtnList = findViewById(R.id.add_time_btn)


        val bundle = this.getIntent().getExtras()
        val crimeId : UUID = bundle?.getSerializable(ARG_CRIME_ID) as UUID
        tmpDuration = bundle.getSerializable("duration") as Int?
        tmpContent = bundle.getSerializable("todo_content") as String?

        noteDetailViewModel.loadCrime(crimeId)

        note = Note()

    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                note.title = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        }
        edTxtTitle.addTextChangedListener(titleWatcher)

        val durationWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if(p0.isNotEmpty()) {
                        note.duration = Integer.parseInt(p0.toString())
                        val f1 = SimpleDateFormat("HH:mm")
                        val date = note.date
                        val hour = note.hour
                        val min = note.min
                        val startTime = min+hour*60 - note.duration
                        val startHour = startTime/60
                        val startMin = startTime % 60
                        if (startHour<10 && startMin >= 10) {
                            txtStart.text = "0$startHour:$startMin"
                        } else if (startHour >= 10 &&startMin<10) {
                            txtStart.text = "$startHour:0$startMin"
                        } else if (startHour<10 && startMin<10) {
                            txtStart.text = "0$startHour:0$startMin"
                        } else {
                            txtStart.text = "$startHour:$startMin"
                        }
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        }
        txtDur.addTextChangedListener(durationWatcher)

        btnDelete.setOnClickListener{
            noteDetailViewModel.deleteCrime(note)
            this.finish()
        }
        btnBack.setOnClickListener{
            this.finish()
        }
        btnAdd15.setOnClickListener{
            var num = Integer.parseInt(txtDur.text.toString())
            num+=15
            txtDur.setText(num.toString())
        }
        btnAdd30.setOnClickListener{
            var num = Integer.parseInt(txtDur.text.toString())
            num+=30
            txtDur.setText(num.toString())
        }
        btnAdd60.setOnClickListener{
            var num = Integer.parseInt(txtDur.text.toString())
            num+=60
            txtDur.setText(num.toString())
        }

        txtStart.setOnClickListener{
            val date = note.date

            var time = note.hour * 60 + note.min - note.duration
            var hour = time/60
            var min = time%60

            if (hour == 0 && min == 0) {
                hour = date.hours
                min = date.minutes
            }
            dialogTimePicker(hour, min, 0)
        }
        txtEnd.setOnClickListener{
            val date = note.date
            var hour = note.hour
            var min = note.min
            if (hour == 0 && min == 0) {
                hour = date.hours
                min = date.minutes
            }
            dialogTimePicker(hour, min, 1)
        }

        noteDetailViewModel.noteLiveData.observe(
            this,
            androidx.lifecycle.Observer { note1 ->
                note1?.let {
                    this.note = note1
                    updateUI()
                }
            })
    }

    private fun updateUI() {

        val formatDate = SimpleDateFormat("yyyy-MM-dd")
        val date = this.note.date
        var hour = note.hour
        var min = note.min

        if (tmpContent != null) {
            note.title = tmpContent as String
        }

        if (note.duration == 0 && tmpDuration != null) {
            note.duration = tmpDuration!!
        }
        if (hour == 0 && min == 0) {
            note.hour = date.hours
            note.min = date.minutes
            hour = date.hours
            min = date.minutes
        }

        val start = min + hour * 60 - note.duration
        val startHour = start/60
        val startMin = start % 60

        todayDate = this.note.date
        strTodayDate = todayDate.toString()

        edTxtTitle.setText(note.title)
        txtDur.setText(note.duration.toString())
        txtDate.text = formatDate.format(todayDate)
        if (startHour<10 && startMin >= 10) {
            txtStart.text = "0$startHour:$startMin"
        } else if (startHour >= 10 &&startMin<10) {
            txtStart.text = "$startHour:0$startMin"
        } else if (startHour<10 && startMin<10) {
            txtStart.text = "0$startHour:0$startMin"
        } else {
            txtStart.text = "$startHour:$startMin"
        }
        if (hour<10 && min>=10) {
            txtEnd.text = "0$hour:$min"
        } else if (hour>=10 && min<10) {
            txtEnd.text = "$hour:0$min"
        } else if (hour<10 && startMin<10) {
            txtEnd.text = "0$hour:0$min"
        } else {
            txtEnd.text = "$hour:$min"
        }
    }
    @SuppressLint("NewApi")
    private fun dialogTimePicker(hour: Int, min: Int, flag: Int) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        // 包含新的api
        val view = inflater.inflate(R.layout.dialog_timepicker, null)

        val timePicker = view.findViewById(R.id.time_picker) as TimePicker
        val btCancel = view.findViewById(R.id.bt_cancel_time) as Button
        val btSure = view.findViewById(R.id.bt_sure_time) as Button

        timePicker.hour = hour
        timePicker.minute = min
        timePicker.setIs24HourView(true)

        val dialog = builder.create()
        dialog.show()
        dialog.getWindow()?.setContentView(view)

        btCancel.setOnClickListener{
            dialog.dismiss()
        }
        btSure.setOnClickListener{
            val nHour = timePicker.hour
            val nMin = timePicker.minute

            when(flag) {
                0 ->{
                    note.duration += (hour - nHour) * 60 + min - nMin
                }
                1->{
                    note.hour = nHour
                    note.min = nMin
                }
            }
            updateUI()
            dialog.dismiss()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        noteDetailViewModel.saveCrime(note)
    }

}