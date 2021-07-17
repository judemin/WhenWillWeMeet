package com.mong.whenwillwemeet

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ModeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actibity_mode)

        var nowUser : userInfo = userInfo()
        if(intent.hasExtra("pid"))
            nowUser.pid = intent.getStringExtra("pid")
        if(intent.hasExtra("name"))
            nowUser.name = intent.getStringExtra("name")



        val actionBar = supportActionBar
        if (actionBar != null)
            actionBar.title = "오랜만이야"

        val createBtn : Button = findViewById(R.id.mode_createBtn)
        val findBtn : Button = findViewById(R.id.mode_findBtn)

        createBtn.setOnClickListener {
            val intent = Intent(this, CreateRoomActivity::class.java)
            intent.putExtra("user", nowUser)
            startActivity(intent)
        }

        findBtn.setOnClickListener {
            val inputCode : EditText = findViewById(R.id.mode_inputcode_et)
            val roomID = inputCode.text

            val database = Firebase.database
            val nowRef = database.getReference("" + roomID)

            nowRef.get().addOnSuccessListener {

                // popup dialog 추가해야 함

                val intent = Intent(this, SelectDayActivity::class.java)
                intent.putExtra("user", nowUser)
                intent.putExtra("roomID", roomID)
                startActivity(intent)

            }.addOnFailureListener {
                makeToast("약속이 없는거 같은데..?ㅠㅜ")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext,"안녕, 다음엔 꼭 한번 보자", Toast.LENGTH_SHORT).show()
    }

    private fun makeToast(msg : String){
        Toast.makeText(applicationContext,"" + msg, Toast.LENGTH_SHORT).show()
    }
}