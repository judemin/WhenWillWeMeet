package com.mong.whenwillwemeet

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CreateRoomActivity : AppCompatActivity() {

    var pid : String? = ""
    var name : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createroom)

        val actionBar = supportActionBar
        if (actionBar != null)
            actionBar.title = "애들 모으자!"

        if(intent.hasExtra("pid"))
            pid = intent.getStringExtra("pid")
        if(intent.hasExtra("name"))
            name = intent.getStringExtra("name")

        val codeTextView : TextView = findViewById(R.id.createRoom_codeTV)
        codeTextView.setText("" + pid)
    }
}