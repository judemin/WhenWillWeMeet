package com.mong.whenwillwemeet

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ModeActivity : AppCompatActivity() {

    var pid : String? = ""
    var name : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actibity_mode)

        val createBtn : Button = findViewById(R.id.mode_createBtn)
        val findBtn : Button = findViewById(R.id.mode_findBtn)

        if(intent.hasExtra("pid"))
            pid = intent.getStringExtra("pid")
        if(intent.hasExtra("name"))
            name = intent.getStringExtra("name")
    }
}