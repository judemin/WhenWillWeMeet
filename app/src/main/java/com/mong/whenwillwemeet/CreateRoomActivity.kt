package com.mong.whenwillwemeet

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class CreateRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createroom)

        val actionBar = supportActionBar
        if (actionBar != null)
            actionBar.title = "애들 모으자!"

        var nowUser = userInfo()
        if(intent.hasExtra("user"))
            nowUser = intent.getParcelableExtra("user")

        val codeTextView : TextView = findViewById(R.id.createRoom_codeTV)
        codeTextView.setText("" + nowUser.pid)

        var nowTime = Calendar.getInstance()

        /// Database Setting ///

        val database = Firebase.database

        /// roomInfo와 방 생성 버튼 터치시 이벤트 처리 ///

        val nowRoomInfo : roomInfo = roomInfo()
        nowRoomInfo._roomID = nowUser.pid
        nowRoomInfo._admin = nowUser.name

        val roomCreateBtn : Button = findViewById(R.id.createRoom_makeRoomBtn)
        roomCreateBtn.setOnClickListener {
            val passwdET: EditText = findViewById(R.id.createRoom_passwdET)
            val startDateP: DatePicker = findViewById(R.id.createRoom_stDateDP)
            val endDateP: DatePicker = findViewById(R.id.createRoom_edDateDP)
            val locationET: EditText = findViewById(R.id.createRoom_locationET)
            val multiSwitch: Switch = findViewById(R.id.createRoom_switchBtn)
            val noticeET: EditText = findViewById(R.id.createRoom_noticeET)

            /// date 검사 ///

            var tmpStCal: Calendar = Calendar.getInstance()
            var tmpEdCal: Calendar = Calendar.getInstance()

            nowRoomInfo._password = passwdET.text.toString()

            tmpStCal.set(startDateP.year,startDateP.month,startDateP.dayOfMonth)
            tmpEdCal.set(endDateP.year,endDateP.month,endDateP.dayOfMonth)

            var dateAfter : Calendar = Calendar.getInstance()
            dateAfter.set(startDateP.year,startDateP.month,startDateP.dayOfMonth)
            dateAfter.add(Calendar.DATE,30)

            if(tmpStCal.timeInMillis >= tmpEdCal.timeInMillis){
                makeToast("날짜를 올바르게 선택해줘!")
            } else if(tmpEdCal.timeInMillis > dateAfter.timeInMillis){
                makeToast("기간은 최대 30일까지 설정 가능해")
            } else if(tmpStCal.timeInMillis < nowTime.timeInMillis) {
                makeToast("날짜를 올바르게 선택해줘!")
            }else {
                /// 나머지 데이터 세팅 ///
                nowRoomInfo._startDate = dateClass(tmpStCal)
                nowRoomInfo._endDate = dateClass(tmpEdCal)

                if (locationET.text.toString().equals(""))
                    nowRoomInfo._location = "온라인"
                else
                    nowRoomInfo._location = locationET.text.toString()

                nowRoomInfo._canMulti = multiSwitch.isChecked

                if (noticeET.text.toString().equals(""))
                    nowRoomInfo._notice = "우리언제만나?"
                else
                    nowRoomInfo._notice = noticeET.text.toString()

                /// 데이터 베이스 INSERT ///

                val nowRef = database.getReference("" + nowUser.pid)

                nowRef.child("room").setValue(nowRoomInfo) // DTO
                nowRef.child("userNum").setValue(1)
                nowRef.child("readyNum").setValue(0)

                // nowRef.child("users").child("" + nowUser.pid).setValue(nowUser) // DTO
                nowRef.child("messeges").setValue("messeges") // DTO

                /// Intent 이동 ///

                val intent = Intent(this, SelectDayActivity::class.java)
                intent.putExtra("user", nowUser)
                intent.putExtra("roomID",nowRoomInfo._roomID)
                startActivity(intent)

                finish()
            }
        }
    }

    private fun makeToast(msg : String){
        Toast.makeText(applicationContext,"" + msg, Toast.LENGTH_SHORT).show()
    }
}