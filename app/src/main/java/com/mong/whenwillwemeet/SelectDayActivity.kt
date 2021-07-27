package com.mong.whenwillwemeet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*


class SelectDayActivity : AppCompatActivity() {

    private val TAG = "SelectDay"

    var roomID = "VXTRURXLIU" ///!///
    var nowUser : userInfo = userInfo()
    var nowRoom : roomInfo = roomInfo()
    //

    lateinit var readyBtn : Button

    //
    lateinit var nowRef : DatabaseReference
    lateinit var database : FirebaseDatabase

    // Firebase에서 가져올 userNum,readyNum
    var userNum : Long = 100
    var readyNum : Long = 0
    private lateinit var readyTV: TextView
    var isReady : Boolean = false
    var isOpenResult : Boolean = false

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

        database = Firebase.database
        nowRef = database.getReference("" + roomID)

        nowRef.child("room").get().addOnSuccessListener {
            var tmp : roomInfo = (it.getValue(roomInfo::class.java) as roomInfo)
            afterRoomSetting(tmp.deepCopy())
        }.addOnFailureListener {
            makeToast("네트워크 오류!")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val builder: Constraints.Builder = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)

        val data: Data.Builder = Data.Builder()
        data.putString("roomID", roomID)
        data.putBoolean("isReady", isReady)
        data.putString("pid", nowUser.pid)

        val uploadWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<deleteWorker>()
            .setInputData(data.build())
            .setConstraints(builder.build())
            .build()

        WorkManager
            .getInstance(applicationContext)
            .enqueue(uploadWorkRequest)
    }

    override fun onResume() {
        super.onResume()
        isOpenResult = false
    }

    private fun afterRoomSetting(roomInfoT: roomInfo){
        nowRoom = roomInfoT

        /// Textview 세팅 ///

        val roomidTV: TextView = findViewById(R.id.selectday_roomid_tv) // roomid clickable하게 => 터치하면 코드 복사
        val noticeTV: TextView = findViewById(R.id.selectday_notice_tv)
        val locationTV: TextView = findViewById(R.id.selectday_location_tv)
        readyTV = findViewById(R.id.selectday_ready_tv)

        roomidTV.text = "약속 코드 : " + nowRoom._roomID
        noticeTV.text = "공지사항 : " + nowRoom._notice
        locationTV.text = "약속 장소 : " + nowRoom._location
        changeRoomNum(1, 0) // database changed 되었을 때 준비된 사람 변경, 현재 방에 있는 사람까지 가져오기

        /// roomNum 동기화 ///

        val numTextListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                changeRoomNum(0, 0)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        database.getReference("" + roomID).child("readyNum").addValueEventListener(numTextListener)
        database.getReference("" + roomID).child("userNum").addValueEventListener(numTextListener)

        /// 캘린더 리사이클러 뷰 세팅 ///

        val recView : RecyclerView = findViewById(R.id.selectday_calendar_rv)

        recView.setHasFixedSize(true)

        val mLayoutManager = LinearLayoutManager(this)
        recView.layoutManager = mLayoutManager

        val calAdapter = calendarAdapter(this)
        recView.adapter = calAdapter

        /// 캘린더 날짜 세팅 ///

        val nowDay : Calendar = Calendar.getInstance()
        val edDay : Calendar = Calendar.getInstance()
        nowDay.set(nowRoom._startDate.year, nowRoom._startDate.month, nowRoom._startDate.day)
        edDay.set(nowRoom._endDate.year, nowRoom._endDate.month, nowRoom._endDate.day)

        while(!isSameDate(nowDay, edDay)){ // Array에 넣을 때 clone
            val nowDate = dateClass(nowDay).deepCopy()
            calAdapter.addData(nowDate)

            nowDay.add(Calendar.DATE, 1)
        }

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
        val chatRecView : RecyclerView = findViewById(R.id.selectday_chat_rv)

        //chatRecView.setHasFixedSize(true)

        val chatLayoutManager = LinearLayoutManager(this);
        chatRecView.layoutManager = chatLayoutManager;

        val chatAdapt = chatAdapter(this)
        chatRecView.adapter = chatAdapt
        // Chatting ChildListener //
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) { // add 되었을 때 trigger
                val comment = dataSnapshot.getValue<msgClass>() as msgClass

                if (comment != null) {
                    val res = chatAdapt.addData(dataSnapshot.key.toString(), comment)
                    if(res)
                        chatRecView.scrollToPosition(chatAdapt.itemCount - 1)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val newComment = dataSnapshot.getValue<msgClass>()
                val commentKey = dataSnapshot.key
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val commentKey = dataSnapshot.key
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

                val movedComment = dataSnapshot.getValue<msgClass>()
                val commentKey = dataSnapshot.key
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                makeToast("Failed to load comments.")
            }
        }
        val charRef = database.getReference("" + roomID).child("messages")
        charRef.addChildEventListener(childEventListener) //!// messages외에 다른 데이터 Add 되었을 때도 처리되는거 수정해야 함

        /// 준비/취소 처리 ///

        readyBtn = findViewById(R.id.selectday_ready_btn)
        readyBtn.setOnClickListener {
            readyBtn.isEnabled = false
            if(!isReady){ // 준비완료 처리
                isReady  = true

                nowRef.child("users").child("" + nowUser.pid).setValue(nowUser)
                readyBtn.text = "아냐 바꿀래"

                changeRoomNum(0, 1)
            } else{ // 취소 처리
                isReady = false

                nowRef.child("users").child("" + nowUser.pid).removeValue()
                readyBtn.text = "다 골랐어"

                changeRoomNum(0, -1)
            }
            readyBtn.isEnabled = true
        }
    }

    private fun changeRoomNum(uNum: Long, rNum: Long){ // 증감시킬 user, ready 두개 넘김
        nowRef.runTransaction(object : Transaction.Handler { // transaction으로 content 데이터 변환
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                var tmpN: Long? = mutableData.child("userNum").value as Long?
                var tmpR: Long? = mutableData.child("readyNum").value as Long?

                if (tmpN != null && tmpR != null) {
                    tmpN += uNum
                    tmpR += rNum

                    mutableData.child("userNum").value = tmpN
                    mutableData.child("readyNum").value = tmpR

                    userNum = tmpN
                    readyNum = tmpR
                }

                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                Log.d(TAG, "postTransaction:onComplete:")

                // Transaction completed
                readyTV.text = "${readyNum}명 준비 / ${userNum}명 "

                if (readyNum == userNum && !isOpenResult)
                    startResult()

                nowRef.push()
            }
        })
    }

    fun startResult(){
        isOpenResult = true
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("roomID", nowRoom._roomID)
        startActivity(intent)
    }

    fun onClickDate(date: dateClass, state: Boolean){
        var nowDate = date
        val dateStr = nowDate.makeKey()

        if(!state)
            nowUser.selectedDates[dateStr] = nowDate.deepCopy()
        else
            nowUser.selectedDates.remove(dateStr)
    }

    private fun isSameDate(src: Calendar, dst: Calendar) : Boolean{
        if(src.get(Calendar.YEAR) != dst.get(Calendar.YEAR))
            return false
        if(src.get(Calendar.MONTH) != dst.get(Calendar.MONTH))
            return false
        if(src.get(Calendar.DATE) != dst.get(Calendar.DATE))
            return false
        return true
    }

    private fun makeToast(msg: String){
        Toast.makeText(applicationContext, "" + msg, Toast.LENGTH_SHORT).show()
    }

    private fun sendMsg(msg: String, sender: String, code: String){
        val msgDTO = msgClass()

        msgDTO.msg = msg
        msgDTO.sender = sender
        msgDTO.senderCode = code
        msgDTO.sendDate = dateClass(Calendar.getInstance())

        nowRef.child("messages").push().setValue(msgDTO)
    }
}