package com.mong.whenwillwemeet

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class SelectDayActivity : AppCompatActivity() {

    private val TAG = "SelectDay"

    var roomID = "FKNHZWSKXX" // roomID for test -> Manifrest 수정 필요, startActivity
    var nowUser : userInfo = userInfo()
    var nowRoom : roomInfo = roomInfo()
    lateinit var nowRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectday)


        /// nowUser for test
        nowUser.name = "민상연"
        nowUser.pid = "ABCDEFGHIJ"
        ///


        val actionBar = supportActionBar
        if (actionBar != null)
            actionBar.title = "그래서 우리 언제 만나?"

        if(intent.hasExtra("user"))
            nowUser = intent.getParcelableExtra("user")

        /// Room Info Firebase ///

        if(intent.hasExtra("roomID"))
            roomID = intent.getStringExtra("roomID")

        val database = Firebase.database
        nowRef = database.getReference("" + roomID)

        nowRef.child("room").get().addOnSuccessListener {
            var tmp : roomInfo = (it.getValue(roomInfo::class.java) as roomInfo)
            afterRoomSetting(tmp.deepCopy())
        }.addOnFailureListener {
            makeToast("네트워크 오류!")
        }
    }

    private fun afterRoomSetting(roomInfoT: roomInfo){
        nowRoom = roomInfoT

        /// Textview 세팅 ///

        val roomidTV: TextView = findViewById(R.id.selectday_roomid_tv) // roomid clickable하게 => 터치하면 코드 복사
        val noticeTV: TextView = findViewById(R.id.selectday_notice_tv)
        val locationTV: TextView = findViewById(R.id.selectday_location_tv)
        val readyTV: TextView = findViewById(R.id.selectday_ready_tv)

        roomidTV.text = "약속 코드 : " + nowRoom._roomID
        noticeTV.text = "공지사항 : " + nowRoom._notice
        locationTV.text = "약속 장소 : " + nowRoom._location
        readyTV.text = "0명 준비 / 1명" // database changed 되었을 때 준비된 사람 변경, 현재 방에 있는 사람까지 가져오기

        /// 캘린더 날짜 세팅 ///

        val nowDay : Calendar = Calendar.getInstance()
        val edDay : Calendar = Calendar.getInstance()
        nowDay.set(nowRoom._startDate.year,nowRoom._startDate.month,nowRoom._startDate.day)
        edDay.set(nowRoom._endDate.year,nowRoom._endDate.month,nowRoom._endDate.day)

        var weekVec = Vector<Calendar>() // 람다식으로 배열 초기화

        while(!isSameDate(nowDay,edDay)){ // Array에 넣을 때 clone
            weekVec.addElement(nowDay.clone() as Calendar?)
            nowDay.add(Calendar.DATE,1)
        }

        /// 캘린더 리사이클러 뷰 세팅 ///

        val calAdapter : calendarAdapter
        val recView : RecyclerView = findViewById(R.id.selectday_calendar_rv)

        recView.setHasFixedSize(true)

        val mLayoutManager = LinearLayoutManager(this);
        recView.layoutManager = mLayoutManager;

        calAdapter = calendarAdapter(weekVec,this)
        recView.adapter = calAdapter

        /// 채팅 세팅 ///

        val chatET : EditText = findViewById(R.id.selectday_chat_et)
        val sendImg : ImageView = findViewById(R.id.selectday_send_iv)

        sendImg.setOnClickListener {
            val etText = chatET.text.toString()

            if(etText != "") {
                sendMsg(etText, nowUser.name, nowUser.pid)
                chatET.setText("")
            }
        }
        // 채팅 리사이클러 뷰 //
        val chatAdapt : chatAdapter
        val chatRecView : RecyclerView = findViewById(R.id.selectday_chat_rv)

        chatRecView.setHasFixedSize(true)

        val chatLayoutManager = LinearLayoutManager(this);
        chatRecView.layoutManager = chatLayoutManager;

        chatAdapt = chatAdapter(this)
        chatRecView.adapter = chatAdapt
        // ChildListener //
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) { // add 되었을 때 trigger
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)
                val comment = dataSnapshot.getValue<msgClass>() as msgClass

                if (comment != null)
                    chatAdapt.addData(comment)

                chatRecView.scrollToPosition(chatAdapt.itemCount - 1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")

                val newComment = dataSnapshot.getValue<msgClass>()
                val commentKey = dataSnapshot.key
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
                val commentKey = dataSnapshot.key
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                val movedComment = dataSnapshot.getValue<msgClass>()
                val commentKey = dataSnapshot.key
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                makeToast("Failed to load comments.")
            }
        }
        nowRef.child("messages").addChildEventListener(childEventListener)

        /// ///


    }

    fun onClickDate(cal : Calendar, checkB : CheckBox){
        var nowDate = dateClass(cal)
        val dateStr = "${nowDate.year}${nowDate.month}${nowDate.day}"

        if(!checkB.isChecked) {
            checkB.isChecked = true
            checkB.buttonTintList = getColorStateList(R.color.baseBlue)

            nowUser.selectedDates.plus(Pair(dateStr,nowDate))
        } else {
            checkB.isChecked = false
            checkB.buttonTintList = getColorStateList(R.color.pastelRed)

            nowUser.selectedDates.minus(dateStr)
        }
    }

    private fun isSameDate(src : Calendar, dst : Calendar) : Boolean{
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

    private fun sendMsg(msg : String, sender : String, code : String){
        val msgDTO = msgClass()

        msgDTO.msg = msg
        msgDTO.sender = sender
        msgDTO.senderCode = code
        msgDTO.sendDate = dateClass(Calendar.getInstance())

        nowRef.child("messages").push().setValue(msgDTO)
    }
}