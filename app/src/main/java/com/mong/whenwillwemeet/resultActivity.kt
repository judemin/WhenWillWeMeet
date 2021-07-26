package com.mong.whenwillwemeet

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
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

    var users = Vector<userInfo>()

    lateinit var nowRef : DatabaseReference
    lateinit var database : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        if(intent.hasExtra("roomID"))
            roomID = intent.getStringExtra("roomID")

        database = Firebase.database
        nowRef = database.getReference("" + roomID)

        /// 결과 리사이클러 뷰 ///

        val resultRecView : RecyclerView = findViewById(R.id.result_rv)

        resultRecView.setHasFixedSize(true)

        val resultLayoutManager = LinearLayoutManager(this);
        resultRecView.layoutManager = resultLayoutManager;

        val resultAdapt : resultAdapter = resultAdapter(this)
        resultRecView.adapter = resultAdapt
        // Result ChildListener //
        nowRef.get().addOnSuccessListener {
            it.children.forEach {
                nowRoom = (it.child("room").getValue(roomInfo::class.java) as roomInfo)

                var user: userInfo = it.child("users").getValue(userInfo::class.java) as userInfo
                users.add(user)
            }

            addUserAdapter()
        }.addOnFailureListener {
            makeToast("네트워크 오류!")
        }
    }

    fun addUserAdapter(){
        userNum = users.size

        val nowDay : Calendar = Calendar.getInstance()
        val edDay : Calendar = Calendar.getInstance()
        nowDay.set(nowRoom._startDate.year, nowRoom._startDate.month, nowRoom._startDate.day)
        edDay.set(nowRoom._endDate.year, nowRoom._endDate.month, nowRoom._endDate.day)

        while(!isSameDate(nowDay, edDay)){
            val now = dateClass(nowDay)
            val dateKey = now.makeKey()

            for(i in users){
                if(i.selectedDates.containsKey(dateKey)){

                }
            }

            nowDay.add(Calendar.DATE, 1)
        }
    }

    private fun makeToast(msg: String){
        Toast.makeText(applicationContext, "" + msg, Toast.LENGTH_SHORT).show()
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
}