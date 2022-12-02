package com.bignerdranch.android.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private lateinit var durTxt : EditText

    private lateinit var nowTxt : TextView
    private lateinit var txtStart: TextView

    private lateinit var btnDate : Button
    private lateinit var btnDelete: Button
    private lateinit var btnBack: Button
    private lateinit var btnAdd15 : Button
    private lateinit var btnAdd30 : Button
    private lateinit var btnAdd60 : Button
    private lateinit var addBtnList : LinearLayout

    private  lateinit var ckbSolved : CheckBox
    private val crimeDetailViewModel : CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime)
        supportActionBar?.hide()

        txtDate = findViewById(R.id.txt_date)

        edTxtTitle = findViewById(R.id.crime_title)
        durTxt = findViewById(R.id.edit_duration)
        nowTxt = findViewById(R.id.edit_now_time)
        txtStart = findViewById(R.id.edit_start_time)

//        btnDate = findViewById(R.id.crime_date)
        btnDelete = findViewById(R.id.btn_delete)
        btnBack = findViewById(R.id.btn_back)

        btnAdd15 = findViewById(R.id.btn_add_15)
        btnAdd30 = findViewById(R.id.btn_add_30)
        btnAdd60 = findViewById(R.id.btn_add_60)
        addBtnList = findViewById(R.id.add_time_btn)

//        ckbSolved = findViewById(R.id.crime_solved)

        note = Note()
        val bundle = this.getIntent().getExtras()
        val crimeId : UUID = bundle?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)

        val f1 = SimpleDateFormat("HH:mm")
        val formatDate = SimpleDateFormat("yyyy-MM-dd")
        val date = this.note.date
        nowTxt.setText(f1.format(date))
        txtDate.text=formatDate.format(date)
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
        durTxt.addTextChangedListener(durationWatcher)


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
            var num = Integer.parseInt(durTxt.text.toString())
            num+=15
            durTxt.setText(num.toString())
        }
        btnAdd30.setOnClickListener{
            var num = Integer.parseInt(durTxt.text.toString())
            num+=30
            durTxt.setText(num.toString())
        }
        btnAdd60.setOnClickListener{
            var num = Integer.parseInt(durTxt.text.toString())
            num+=60
            durTxt.setText(num.toString())
        }

        txtStart.setOnClickListener{
            dialogTimePicker()
        }
        nowTxt.setOnClickListener{
            dialogTimePicker()
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
    private fun updateUI() {
        edTxtTitle.setText(note.title)
        durTxt.setText(note.duration.toString())

        val f1 = SimpleDateFormat("HH:mm")
        val date = this.note.date
        val startTime = Date(date.time - note.duration * 60 * 1000)
        txtStart.setText(f1.format(startTime))
        nowTxt.setText(f1.format(date))
//        btnDate.text = crime.date.toString()
//        ckbSolved.apply {
//            isChecked = note.isSolved
//            jumpDrawablesToCurrentState()
//        }
    }
    private fun dialogTimePicker() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_timepicker, null);
        val dialog = builder.create();
        dialog.show();
        dialog.getWindow()?.setContentView(view);
    }
    override fun onDestroy() {
        super.onDestroy()
        crimeDetailViewModel.saveCrime(note)
    }



}