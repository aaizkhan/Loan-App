package com.aaiztech.loanapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class SettingActivity : AppCompatActivity() {
    lateinit var sharedPrefrence: SharedPreferences;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        sharedPrefrence=getSharedPreferences("isLogin", MODE_PRIVATE)

    }

    fun btnBack(view: android.view.View) {
        finish()
    }

    fun btnLogout(view: android.view.View) {
        var editor=sharedPrefrence.edit();
        editor.putBoolean("isLogin",false);
        editor.commit()
        editor.apply()

        Toast.makeText(applicationContext,"Logout Successfully",Toast.LENGTH_SHORT).show()
        var intent=Intent(applicationContext,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}