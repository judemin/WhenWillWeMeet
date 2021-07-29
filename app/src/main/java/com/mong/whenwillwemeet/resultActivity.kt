package com.mong.whenwillwemeet

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class ResultActivity : AppCompatActivity() {
    private val TAG = "ResultActivity"

    var roomID = ""
    var userNum = 0

    lateinit var nowRoom : roomInfo

    lateinit var actionBar : ActionBar

    var users = Vector<userInfo>()

    lateinit var nowRef : DatabaseReference
    lateinit var database : FirebaseDatabase

    var isNone = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        actionBar = supportActionBar!!
        if (actionBar != null)
            actionBar.title = "이때 만나자!"

        if(intent.hasExtra("roomID"))
            roomID = intent.getStringExtra("roomID")

        /// Database get data ///

        database = Firebase.database
        nowRef = database.getReference("" + roomID)

        nowRef.child("room").get().addOnSuccessListener {
            nowRoom = (it.getValue(roomInfo::class.java) as roomInfo)
            getUsers()
        }.addOnFailureListener {
            makeToast("네트워크 오류!")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_result, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == R.id.menu_result_share_btn){
            var shareStr = ""
            // TODO
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun getUsers(){
        nowRef.child("users").get().addOnSuccessListener {

            it.children.forEach {
                var user: userInfo = it.getValue(userInfo::class.java) as userInfo
                users.add(user)
            }

            addUserAdapter()

            if(isNone)
                if (actionBar != null)
                    actionBar.title = "겹치는 날짜가 없나본데 우리..?ㅠㅜ"

        }.addOnFailureListener {
            if (actionBar != null)
                actionBar.title = "준비된 사람이 없는거 같아..ㅠㅜ"
        }
    }

    private fun addUserAdapter(){
        userNum = users.size

        /// 결과 리사이클러 뷰 ///

        val resultRecView : RecyclerView = findViewById(R.id.result_rv)

        resultRecView.setHasFixedSize(true)

        val resultLayoutManager = LinearLayoutManager(this);
        resultRecView.layoutManager = resultLayoutManager;

        val resultAdapt = resultAdapter(this)
        resultRecView.adapter = resultAdapt

        /// Date Count ///

        val nowDay : Calendar = Calendar.getInstance()
        val edDay : Calendar = Calendar.getInstance()
        nowDay.set(nowRoom._startDate.year, nowRoom._startDate.month, nowRoom._startDate.day)
        edDay.set(nowRoom._endDate.year, nowRoom._endDate.month, nowRoom._endDate.day)

        while(!isSameDate(nowDay, edDay)){
            val now = dateClass(nowDay)
            val dateKey = now.makeKey()

            for(i in users)
                if(i.selectedDates.containsKey(dateKey)) {
                    now.selectedUser.add(i.name)
                    isNone = false
                }

            now.selectedNum = now.selectedUser.size

            if(now.selectedNum != 0)
                resultAdapt.addData(now)
            nowDay.add(Calendar.DATE, 1)
        }
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
}