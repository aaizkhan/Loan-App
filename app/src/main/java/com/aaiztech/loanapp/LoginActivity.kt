package com.aaiztech.loanapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    lateinit var edtUsername: EditText;
    lateinit var edtPassword: EditText;
    lateinit var btnLogin: Button;
    lateinit var sharedPrefrence:SharedPreferences;


    override fun onStart() {
        super.onStart()
        sharedPrefrence=getSharedPreferences("isLogin", MODE_PRIVATE)
        var isLogin=sharedPrefrence.getBoolean("isLogin",false)
        if(isLogin){
            var intent=Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtUsername = findViewById<EditText>(R.id.edt_username)
        edtPassword = findViewById<EditText>(R.id.edt_password)
        btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener {
            if (edtUsername.text.toString().equals("")) {
                edtUsername.setError("Enter Username")
                return@setOnClickListener
            }
            if (!edtUsername.text.toString().equals("Hotpotato")) {
                edtUsername.setError("Incorrect username")
                return@setOnClickListener
            }
            if (edtPassword.text.toString().equals("")) {
                edtPassword.setError("Enter Password")
                return@setOnClickListener
            }
           if (!edtPassword.text.toString().equals("Ma5hed!")) {
                edtPassword.setError("Incorrect Password")
               return@setOnClickListener
            }

            Toast.makeText(applicationContext, "Login", Toast.LENGTH_SHORT).show()
            var editor=sharedPrefrence.edit();
            editor.putBoolean("isLogin",true);
            editor.commit()
            editor.apply()

            var intent=Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}