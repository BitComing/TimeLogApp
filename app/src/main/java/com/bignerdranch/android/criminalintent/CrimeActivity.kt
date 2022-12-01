package com.bignerdranch.android.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.criminalintent.fragment.CrimeFragment
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

    private lateinit var crime : Crime
    private lateinit var photoFile: File

    private lateinit var edTxtTitle : EditText
    private lateinit var durTxt : EditText
    private lateinit var nowTxt : EditText

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

        edTxtTitle = findViewById(R.id.crime_title)
        durTxt = findViewById(R.id.edit_duration)
        nowTxt = findViewById(R.id.edit_now_time)

        btnDate = findViewById(R.id.crime_date)
        btnDelete = findViewById(R.id.btn_delete)
        btnBack = findViewById(R.id.btn_back)

        btnAdd15 = findViewById(R.id.btn_add_15)
        btnAdd30 = findViewById(R.id.btn_add_30)
        btnAdd60 = findViewById(R.id.btn_add_60)
        addBtnList = findViewById(R.id.add_time_btn)

        ckbSolved = findViewById(R.id.crime_solved)

        crime = Crime()
        val bundle = this.getIntent().getExtras()
        val crimeId : UUID = bundle?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)

        val f1 = SimpleDateFormat("HH:mm")
        val date = this.crime.date
        nowTxt.setText(f1.format(date))
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                crime.title = p0.toString()
                Log.d(TAG,"I had changed")
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
                        crime.duration = Integer.parseInt(p0.toString())
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        }
        durTxt.addTextChangedListener(durationWatcher)
        durTxt.setText("0")

        ckbSolved.apply {
            setOnCheckedChangeListener{_,isChecked ->
                crime.isSolved = isChecked
            }
        }
//        btnDate.setOnClickListener {
//            DatePickerFragment.newInstance(crime.date).apply {
//                setTargetFragment(this, REQUEST_DATE)
//                show(this.requireFragmentManager(), DIALOG_DATE)
//            }
//        }
        btnDelete.setOnClickListener{
            crimeDetailViewModel.deleteCrime(crime)
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

        crimeDetailViewModel.crimeLiveData.observe(
            this,
            androidx.lifecycle.Observer { crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        crimeDetailViewModel.saveCrime(crime)
    }
    private fun updateUI() {
        edTxtTitle.setText(crime.title)
        btnDate.text = crime.date.toString()
        ckbSolved.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

}