package com.mong.whenwillwemeet

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class resultAdapter (private val resultAct: ResultActivity) :
    RecyclerView.Adapter<resultAdapter.ViewHolder>() {
    private val dataSet: Vector<dateClass> = Vector()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var resultCL : ConstraintLayout
        var yearTV: TextView
        var dayTV: TextView
        var infoTV: TextView

        var availLL : LinearLayout
        var availTV: TextView

        init {
            resultCL = view.findViewById(R.id.row_result_date_cl)
            yearTV = view.findViewById(R.id.row_result_yearTV)
            dayTV = view.findViewById(R.id.row_result_dayTV)
            infoTV = view.findViewById(R.id.row_result_infoTV)

            availLL = view.findViewById(R.id.row_result_available_ll)
            availTV = view.findViewById(R.id.row_result_available_tv)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_result, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val nowDate : dateClass = dataSet[position]

        viewHolder.yearTV.text = "${nowDate.year}년 ${nowDate.month + 1}월"
        viewHolder.dayTV.text = "${nowDate.day}일 " +
                "(${dateClass.dayofweek[nowDate.dayOfWeek - 1]})"

        val selectNum = nowDate.selectedNum
        val userNum = resultAct.userNum
        viewHolder.infoTV.text = "${selectNum}명 / ${userNum}명"

        var tmpStr = ""
        for(i in 0 until nowDate.selectedUser.size) // 0 until n -> 0 < n,   0..n -> 0 <= n
            tmpStr += nowDate.selectedUser[i] + "\n"
        tmpStr = tmpStr.removeRange(tmpStr.length-1,tmpStr.length)
        viewHolder.availTV.text = tmpStr

        // FF ~ 00 => 256
        val colorCode : Int = ((selectNum.toDouble() / userNum.toDouble()) * 256).toInt() - 1
        var colorString : String = "#" + Integer.toHexString(colorCode) + "ADD1FC"
        if(colorCode < 0)
            colorString = "#00ADD1FC"

        viewHolder.resultCL.setBackgroundColor(Color.parseColor(colorString))
    }

    fun addData(tmp : dateClass){
        dataSet.add(tmp)
        notifyItemInserted(dataSet.size - 1)
    }

    override fun getItemCount() = dataSet.size

}