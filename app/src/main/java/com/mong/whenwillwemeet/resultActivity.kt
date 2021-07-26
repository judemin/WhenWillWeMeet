package com.mong.whenwillwemeet

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ResultActivity : AppCompatActivity() {

    var roomID = ""
    var userNum = 0
    var users = Vector<userInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        if(intent.hasExtra("roomID"))
            roomID = intent.getStringExtra("roomID")
    }
}