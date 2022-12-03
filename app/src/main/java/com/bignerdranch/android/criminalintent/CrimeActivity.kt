package com.bignerdranch.android.criminalintent

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
//import com.bnuz.example.criminalintentRoom.CrimeFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "CrimeActivity"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
class CrimeActivity : AppCompatActivity() {

//    interface Callbacks {
//        fun onCrimeDeleted(fragment: CrimeFragment)
//    }
//
//    private var callbacks: Callbacks? = null

    private lateinit var note : Note
    private lateinit var photoFile: File

    private lateinit var txtDate: TextView

    private lateinit var edTxtTitle : EditText
    private lateinit var txtDur : EditText

    private lateinit var txtEnd : TextView
    private lateinit var txtStart: TextView

//    private lateinit var btnDate : Button
//    private  lateinit var ckbSolved : CheckBox
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

    private val crimeDetailViewModel : CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
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

//        btnDate = findViewById(R.id.crime_date)
//        ckbSolved = findViewById(R.id.crime_solved)
        btnDelete = findViewById(R.id.btn_delete)
        btnBack = findViewById(R.id.btn_back)

        btnAdd15 = findViewById(R.id.btn_add_15)
        btnAdd30 = findViewById(R.id.btn_add_30)
        btnAdd60 = findViewById(R.id.btn_add_60)
        addBtnList = findViewById(R.id.add_time_btn)


        val bundle = this.getIntent().getExtras()
        val crimeId : UUID = bundle?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)

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
                        val startTime = Date(date.time - note.duration * 60 * 1000)
                        txtStart.setText(f1.format(startTime))
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        }
        txtDur.addTextChangedListener(durationWatcher)


//        ckbSolved.apply {
//            setOnCheckedChangeListener{_,isChecked ->
//                note.isSolved = isChecked
//            }
//        }
//        btnDate.setOnClickListener {
//            DatePickerFragment.newInstance(crime.date).apply {
//                setTargetFragment(this, REQUEST_DATE)
//                show(this.requireFragmentManager(), DIALOG_DATE)
//            }
//        }
        btnDelete.setOnClickListener{
            crimeDetailViewModel.deleteCrime(note)
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
//            val date = Date(note.date.time - note.duration )
//            val hour = date.hours
//            val min = date.minutes
//            dialogTimePicker(hour, min, 0)

        }
        txtEnd.setOnClickListener{
            val date = note.date
            val hour = date.hours
            val min = date.minutes
            dialogTimePicker(hour, min, 1)
        }

        crimeDetailViewModel.noteLiveData.observe(
            this,
            androidx.lifecycle.Observer { note1 ->
                note1?.let {
                    this.note = note1
                    updateUI()
                }
            })
    }
    // 真正的初始化地方
    private fun updateUI() {

        val formatDate = SimpleDateFormat("yyyy-MM-dd")
        val fTime = SimpleDateFormat("HH:mm")
        val date = this.note.date
        note.hour = note.date.hours
        note.min = note.date.minutes
        val hour = note.hour
        val min = note.min

        val startTime = Date(date.time - note.duration * 60 * 1000)
        todayDate = this.note.date
        strTodayDate = todayDate.toString()


        edTxtTitle.setText(note.title)
        txtDur.setText(note.duration.toString())
//        txtEnd.text = fTime.format(todayDate)
        txtEnd.text = fTime.format(todayDate)
        txtDate.text = formatDate.format(todayDate)
        txtStart.text = fTime.format(startTime)
        Log.d(TAG,"$hour:$min")

//        ckbSolved.apply {
//            isChecked = note.isSolved
//            jumpDrawablesToCurrentState()
//        }
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
//            val formatDate = SimpleDateFormat("yyyy-MM-dd")
//            val tmpDate = formatDate.format(todayDate).toString()

            val hour = timePicker.hour
            val min = timePicker.minute
            val time = "$hour:$min"

//            crimeDetailViewModel.noteLiveData.observe(
//                this,
//                androidx.lifecycle.Observer { note1 ->
//                    note1?.let {
//                        it.hour = hour
//                        it.min = min
//                        this.note = it
//                        updateUI()
//                    }
//                })
//            val f = SimpleDateFormat("HH:mm")
//            txtEnd.text = time
//
//            val tim = f.parse(txtEnd.text.toString()).time - f.parse(txtStart.text.toString()).time
//            note.duration = tim.toInt()
//            txtDur.setText(f.format(note.duration))

//            note.date =
//            Log.d(TAG, f.parse(txtEnd.text.toString()).time.toString())
            dialog.dismiss()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        crimeDetailViewModel.saveCrime(note)
    }

}