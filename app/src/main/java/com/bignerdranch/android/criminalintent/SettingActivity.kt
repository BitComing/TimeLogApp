package com.bignerdranch.android.criminalintent

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.criminalintent.R


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.hide()
    }
    fun onClick(view: View) {
        when (view.id) {
            R.id.setting_iv_back -> finish()
            R.id.setting_tv_clear -> showDeleteDialog()
        }
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("测试")
            .setMessage("测试")
            .setPositiveButton("取消", null)
        builder.create().show()
    }
}