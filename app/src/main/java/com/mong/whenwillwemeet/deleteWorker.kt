package com.mong.whenwillwemeet

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class deleteWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    lateinit var nowRef : DatabaseReference
    lateinit var database : FirebaseDatabase
    lateinit var pid : String

    var nowUser : Long = 0

    override fun doWork(): Result {
        val roomID = inputData.getString("roomID")
        val isReady = inputData.getBoolean("isReady",false)
        pid = inputData.getString("pid").toString()

        database = Firebase.database
        nowRef = database.getReference("" + roomID)

        if(isReady){
            changeRoomNum(-1,-1)
        } else{
            changeRoomNum(-1,0)
        }

        return Result.success()
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

                    nowUser = tmpN
                }

                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                nowRef.child("users").child("" + pid).removeValue()
                // tmpN 검사해서 현재 방에 있는 사람이 0명이면 방 삭제
                    /*
                if(nowUser.equals(0))
                    nowRef.removeValue()
                     */
            }
        })
    }
}