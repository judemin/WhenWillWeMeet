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

        // 결과 리사이클러 뷰 //
        val resultRecView : RecyclerView = findViewById(R.id.result_rv)

        resultRecView.setHasFixedSize(true)

        val resultLayoutManager = LinearLayoutManager(this);
        resultRecView.layoutManager = resultLayoutManager;

        val resultAdapt : resultAdapter = resultAdapter(this)
        resultRecView.adapter = resultAdapt
        // Result ChildListener //
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) { // add 되었을 때 trigger
                val comment = dataSnapshot.getValue<userInfo>() as userInfo

                if (comment != null) {
                    userNum++
                    users.add(comment)
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
        nowRef.child("users").addChildEventListener(childEventListener)

        makeToast("")
    }

    private fun makeToast(msg: String){
        Toast.makeText(applicationContext, "" + msg, Toast.LENGTH_SHORT).show()
    }
}