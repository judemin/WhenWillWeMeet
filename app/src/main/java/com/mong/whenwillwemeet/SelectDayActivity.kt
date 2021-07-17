package com.mong.whenwillwemeet

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SelectDayActivity : AppCompatActivity() {

    var roomID = ""
    var nowUser : userInfo = userInfo()
    var nowRoom : roomInfo = roomInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectday)

        val actionBar = supportActionBar
        if (actionBar != null)
            actionBar.title = "그래서 우리 언제 만나?"

        if(intent.hasExtra("user"))
            nowUser = intent.getParcelableExtra("user")

        /// Room Info Firebase ///

        if(intent.hasExtra("roomID"))
            roomID = intent.getStringExtra("roomID")

        val database = Firebase.database
        val nowRef = database.getReference("" + roomID)

        nowRef.child("room").get().addOnSuccessListener {
            nowRoom = it.getValue(roomInfo::class.java) as roomInfo
        }.addOnFailureListener {
            makeToast("데이터베이스 연결 오류!")
        }

        /// Textview 세팅 ///

        val roomidTV: TextView = findViewById(R.id.selectday_roomid_tv) // roomid clickable하게 => 터치하면 코드 복사
        val noticeTV: TextView = findViewById(R.id.selectday_notice_tv)
        val locationTV: TextView = findViewById(R.id.selectday_location_tv)

        roomidTV.setText("약속 코드 : " + nowRoom._roomID)
        noticeTV.setText("공지사항 : " + nowRoom._notice)
        locationTV.setText("약속 장소 : " + nowRoom._location)

        ///  ///
    }

    private fun makeToast(msg : String){
        Toast.makeText(applicationContext,"" + msg, Toast.LENGTH_SHORT).show()
    }
}