package com.mossonthetree.chatappkotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UserName : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_name)

        val prefs = getSharedPreferences("chat", 0);

        findViewById<Button>(R.id.btnSave).setOnClickListener {v ->
            val userName = findViewById<EditText>(R.id.edtUserName).text.toString()
            if(userName.isNotEmpty()) {
                val editor = prefs.edit()
                editor.putString("user_name", userName)
                editor.apply()
                val resultData = Intent()
                resultData.putExtra("user_name", userName)
                setResult(Activity.RESULT_OK, resultData)
                finish()
            } else {
                Toast.makeText(this, "Please enter a user name", Toast.LENGTH_LONG).show()
            }
        }
    }
}