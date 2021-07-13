package com.mong.whenwillwemeet

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase

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

        private lateinit var database: DatabaseReference
        Firebase.database

        /// roomInfo와 방 생성 버튼 터치시 이벤트 처리 ///
        val nowRoomInfo : RoomInfo = RoomInfo()
        nowRoomInfo._roomID = pid.toString()
        nowRoomInfo._admin = name.toString()

        val roomCreateBtn : Button = findViewById(R.id.createRoom_makeRoomBtn)
        roomCreateBtn.setOnClickListener {
            val passwdET : EditText = findViewById(R.id.createRoom_passwdET)
            val startDateP : DatePicker = findViewById(R.id.createRoom_stDateDP)
            val endDateP : DatePicker = findViewById(R.id.createRoom_edDateDP)
            val locationET : EditText = findViewById(R.id.createRoom_locationET)
            val multiSwitch : Switch = findViewById(R.id.createRoom_switchBtn)
            val noticeET : EditText = findViewById(R.id.createRoom_noticeET)
        }
    }
}