package com.bignerdranch.android.timelog.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bignerdranch.android.timelog.R
import com.bignerdranch.android.timelog.SettingActivity

class SettingFragment : Fragment() {
    private lateinit var linearSetting: RelativeLayout
    private lateinit var layoutUpdate: RelativeLayout
    private lateinit var layoutAbout: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        linearSetting = view.findViewById(R.id.linear_setting)
        layoutUpdate = view.findViewById(R.id.tab_update)
        layoutAbout = view.findViewById(R.id.tab_tmp)
        return view
    }

    override fun onStart() {
        super.onStart()
        linearSetting.setOnClickListener{
            val intent = Intent(this@SettingFragment.activity, SettingActivity::class.java)
            startActivity(intent)
        }
        layoutUpdate.setOnClickListener{
            Toast.makeText(this.context,"暂无更新",Toast.LENGTH_SHORT).show()
        }
        layoutAbout.setOnClickListener{
            Toast.makeText(this.context,"未开发",Toast.LENGTH_SHORT).show()
        }
    }
}