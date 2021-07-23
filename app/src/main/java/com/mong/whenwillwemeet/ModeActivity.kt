package com.mong.whenwillwemeet

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class ModeActivity : AppCompatActivity() {

    var nowUser : userInfo = userInfo()
    var roomID : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actibity_mode)

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
            createBtn.isEnabled = true

            val intent = Intent(this, CreateRoomActivity::class.java)
            intent.putExtra("user", nowUser)
            startActivity(intent)

            createBtn.isEnabled = false
        }

        findBtn.setOnClickListener {
            findBtn.isEnabled = false

            val inputCode : EditText = findViewById(R.id.mode_inputcode_et)
            roomID = inputCode.text.toString()

            val database = Firebase.database
            val nowRef = database.getReference("" + roomID)

            nowRef.child("room").get().addOnSuccessListener {

                if(it.getValue() == null)
                    makeToast("약속이 없는거 같은데..?ㅠㅜ")
                else{
                    var passwd : String = it.child("_password").getValue() as String

                    if(passwd.equals("")){
                        startSelectDay(nowUser, roomID)
                    }else {
                        val builder = AlertDialog.Builder(this)
                        val dialogView = layoutInflater.inflate(R.layout.dialog_password, null)

                        builder.setView(dialogView)
                            .setPositiveButton("확인") { dialogInterface, i ->

                                var passwdET : EditText = dialogView.findViewById<EditText>(R.id.dialog_passwd_et)
                                if (passwd.equals(passwdET.text.toString())) // EditText에서 toString이 문제
                                    startSelectDay(nowUser, roomID)
                                else
                                    makeToast("비밀번호 다시 확인해봐!")
                            }
                            .setNegativeButton("취소") { dialogInterface, i ->
                                // 취소일때 아무 액션 없음
                            }
                            .show()
                    }
                }

            }.addOnFailureListener {
                makeToast("네트워크 오류!")
            }
            findBtn.isEnabled = true
        }
    }

    private fun startSelectDay(nowUser : userInfo, roomID : String){
        val intent = Intent(this, SelectDayActivity::class.java)
        intent.putExtra("user", nowUser)
        intent.putExtra("roomID", roomID)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext,"안녕, 다음엔 꼭 한번 보자", Toast.LENGTH_SHORT).show()
    }

    private fun makeToast(msg : String){
        Toast.makeText(applicationContext,"" + msg, Toast.LENGTH_SHORT).show()
    }
}