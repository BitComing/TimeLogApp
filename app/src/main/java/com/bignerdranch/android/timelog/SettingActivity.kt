package com.bignerdranch.android.timelog

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.timelog.R


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