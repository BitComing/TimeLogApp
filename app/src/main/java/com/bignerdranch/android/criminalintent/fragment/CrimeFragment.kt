package com.bignerdranch.android.criminalintent.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.criminalintent.*
import java.io.File
import java.lang.String.format
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_CRIME_ID = "crime_id"
private const val TAG = "CrimeFragment"

private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val REQUEST_CONTACT = 1
private const val REQUEST_PHOTO = 2
private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeFragment : Fragment() , DatePickerFragment.Callbacks{

    interface Callbacks {
        fun onCrimeDeleted(fragment: CrimeFragment)
    }

    private var callbacks: Callbacks? = null

//    private var param1: String? = null
//    private var param2: String? = null


    private lateinit var crime : Crime
    private lateinit var photoFile: File

    private lateinit var edTxtTitle : EditText
    private lateinit var durTxt : EditText
    private lateinit var nowTxt : EditText

    private lateinit var btnDate : Button
    private lateinit var btnDelete: Button
    private lateinit var btnAdd15 : Button
    private lateinit var btnAdd30 : Button
    private lateinit var btnAdd60 : Button
    private lateinit var addBtnList : LinearLayout

    private  lateinit var ckbSolved : CheckBox
//    private lateinit var reportButton : Button
//    private lateinit var suspectButton: Button
//    private lateinit var photoButton: ImageButton
//    private lateinit var photoView: ImageView
//    private lateinit var photoUri: Uri


    private val crimeDetailViewModel : CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }

        crime = Crime()
        val crimeId : UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_crime, container, false)
        edTxtTitle = view.findViewById(R.id.crime_title) as EditText
        durTxt = view.findViewById(R.id.edit_duration)
        nowTxt = view.findViewById(R.id.edit_now_time)

        btnDate = view.findViewById(R.id.crime_date) as Button
        btnDelete = view.findViewById(R.id.btn_delete_note)

        btnAdd15 = view.findViewById(R.id.btn_add_15)
        btnAdd30 = view.findViewById(R.id.btn_add_30)
        btnAdd60 = view.findViewById(R.id.btn_add_60)
        addBtnList = view.findViewById(R.id.add_time_btn)

        ckbSolved = view.findViewById(R.id.crime_solved) as CheckBox

        val f1 = SimpleDateFormat("HH:mm")
        val date = this.crime.date
        nowTxt.setText(f1.format(date))

//        reportButton = view.findViewById(R.id.crime_report) as Button
//        suspectButton = view.findViewById(R.id.crime_suspect) as Button
//        photoButton = view.findViewById(R.id.crime_camera) as ImageButton
//        photoView = view.findViewById(R.id.crime_photo) as ImageView
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { crime ->
                crime?.let {
                    this.crime = crime
//                    photoFile = crimeDetailViewModel.getPhotoFile(crime)
//                    photoUri = FileProvider.getUriForFile(requireActivity(),
//                        "com.bignerdranch.android.criminalintent.fileprovider",
//                        photoFile)
                    updateUI()
                }
            })
    }


    override fun onStart(){
        super.onStart()

        val titleWatcher = object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                crime.title = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        }
        edTxtTitle.addTextChangedListener(titleWatcher)

        val durationWatcher = object :TextWatcher{
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

        ckbSolved.apply {
            setOnCheckedChangeListener{_,isChecked ->
                crime.isSolved = isChecked
            }
        }
        btnDate.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)
                show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }
        btnDelete.setOnClickListener{
            crimeDetailViewModel.deleteCrime(crime)
            callbacks?.onCrimeDeleted(this@CrimeFragment)
        }
        addBtnList.setOnClickListener{
            var num = Integer.parseInt(durTxt.text.toString())
            when(it.id) {
                R.id.btn_add_15-> num+=15
                R.id.btn_add_30-> num+=30
                R.id.btn_add_60-> num+=60
            }
            durTxt.setText(num.toString())
        }
//        reportButton.setOnClickListener {
//            Intent(Intent.ACTION_SEND).apply {
//                type = "text/plain"
//                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
//                putExtra(
//                    Intent.EXTRA_SUBJECT,
//                    getString(R.string.crime_report_subject)
//                )
//            }.also { intent ->
//                val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report))
//                startActivity(chooserIntent)
//            }
//        }
//
//        suspectButton.apply {
//            val pickContactIntent = Intent(Intent.ACTION_PICK,
//            ContactsContract.Contacts.CONTENT_URI)
//            setOnClickListener {
//                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
//            }
////            pickContactIntent.addCategory(Intent.CATEGORY_HOME)
//            val packageManager: PackageManager = requireActivity().packageManager
//            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(pickContactIntent,
//            PackageManager.MATCH_DEFAULT_ONLY)
//            if(resolvedActivity == null) {
//                isEnabled = false
//            }
//        }

//        photoButton.apply {
//            val packageManager: PackageManager = requireActivity().packageManager
//            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(captureImage,
//                    PackageManager.MATCH_DEFAULT_ONLY)
//            if (resolvedActivity == null) {
//                isEnabled = false
//            }
//            setOnClickListener {
//                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//                val cameraActivities: List<ResolveInfo> =
//                    packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
//                for (cameraActivity in cameraActivities) {
//                    requireActivity().grantUriPermission(
//                        cameraActivity.activityInfo.packageName,
//                        photoUri,
//                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                }
//                startActivityForResult(captureImage, REQUEST_PHOTO)
//            }
//        }

    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
    override fun onStop(){
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    // 重写接口方法
    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }

    private fun updateUI() {
//        Log.d(TAG,"************* $crime.title *************")
        edTxtTitle.setText(crime.title)
        btnDate.text = crime.date.toString()
        ckbSolved.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
//        if(crime.suspect.isNotEmpty()) {
//            suspectButton.text = crime.suspect
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_CONTACT && data != null ->
            {
                val contactUri: Uri? = data.data
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val cursor = contactUri?.let {
                    requireActivity().contentResolver
                        .query(
                            it, queryFields, null,
                            null, null)
                }
                cursor?.use {
                    if (it.count == 0) {
                        return
                    }
                    it.moveToFirst()
                    val suspect = it.getString(0)
                    crime.suspect = suspect
                    crimeDetailViewModel.saveCrime(crime)
//                    suspectButton.text = suspect
                }
            }
        }
    }

    private fun getCrimeReport(): String {
        val solvedString = if(crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val dateString = DateFormat.format(DATE_FORMAT,crime.date).toString()

        var suspect = if(crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string .crime_report_suspect)
        }
        return getString(R.string.crime_report,crime.title,
        dateString,solvedString,suspect)
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CrimeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        fun newInstance(crimeId : UUID) :CrimeFragment {
            val args = Bundle().apply{
                putSerializable(ARG_CRIME_ID,crimeId)
            }
            return CrimeFragment().apply { arguments = args }
        }
    }
}