package com.mong.whenwillwemeet

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class SelectDayActivity : AppCompatActivity() {

    var roomID = "FKNHZWSKXX" // roomID for test -> Manifrest 수정 필요
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
            var tmp : roomInfo = (it.getValue(roomInfo::class.java) as roomInfo)
            afterRoomSetting(tmp.deepCopy())
        }.addOnFailureListener {
            makeToast("네트워크 오류!")
        }
    }

    fun afterRoomSetting(roomInfoT: roomInfo){
        nowRoom = roomInfoT

        /// Textview 세팅 ///

        val roomidTV: TextView = findViewById(R.id.selectday_roomid_tv) // roomid clickable하게 => 터치하면 코드 복사
        val noticeTV: TextView = findViewById(R.id.selectday_notice_tv)
        val locationTV: TextView = findViewById(R.id.selectday_location_tv)

        roomidTV.setText("약속 코드 : " + nowRoom._roomID)
        noticeTV.setText("공지사항 : " + nowRoom._notice)
        locationTV.setText("약속 장소 : " + nowRoom._location)

        /// 캘린더 세팅 ///
        // SelectDay에서 dateClass st ed 데이터 바탕으로 캘린더 7개 만들어서 보내줌
        // 7개씩 LinearLayout + monthLayout => weekAdapter들을 Adapter에
        val calAdapter : calendarAdapter
        val recView : RecyclerView = findViewById(R.id.selectday_calendar_rv)

        val nowDay : Calendar = Calendar.getInstance()
        val edDay : Calendar = Calendar.getInstance()
        nowDay.set(nowRoom._startDate.year,nowRoom._startDate.month,nowRoom._startDate.day)
        edDay.set(nowRoom._endDate.year,nowRoom._endDate.month,nowRoom._endDate.day)

        var weekArr : Array<Calendar> = Array(1){ Calendar.getInstance()} // 람다식으로 배열 초기화

        var weekCnt = 0
        while(!isSameDate(nowDay,edDay)){ // Array에 넣을 때 clone
            weekArr.
            weekArr[weekCnt] = nowDay.clone()
            nowDay.add(Calendar.DATE,1)

            if(dayCnt == 7){
                weekCnt++
                dayCnt = 0
            }
        }

        recView.adapter = calAdapter

    }

    fun isSameDate(src : Calendar,dst : Calendar) : Boolean{
        if(src.get(Calendar.YEAR) != dst.get(Calendar.YEAR))
            return false
        if(src.get(Calendar.MONTH) != dst.get(Calendar.MONTH))
            return false
        if(src.get(Calendar.DATE) != dst.get(Calendar.DATE))
            return false
        return true
    }

    private fun makeToast(msg : String){
        Toast.makeText(applicationContext,"" + msg, Toast.LENGTH_SHORT).show()
    }
}