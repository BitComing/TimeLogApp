package com.bignerdranch.android.criminalintent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.SettingActivity

class SettingFragment : Fragment() {
    private lateinit var linearSetting: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        linearSetting = view.findViewById(R.id.linear_setting)
        return view
    }

    override fun onStart() {
        super.onStart()
        linearSetting.setOnClickListener{
            val intent = Intent(this@SettingFragment.activity, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}