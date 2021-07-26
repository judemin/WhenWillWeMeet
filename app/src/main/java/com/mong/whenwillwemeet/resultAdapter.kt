package com.mong.whenwillwemeet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class resultAdapter (private val resultAct: ResultActivity) :
    RecyclerView.Adapter<resultAdapter.ViewHolder>() {
    private val dataSet: Vector<dateClass> = Vector()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var resultLL : LinearLayout
        var yearTV: TextView
        var dayTV: TextView
        var infoTV: TextView

        init {
            resultLL = view.findViewById(R.id.row_result_ll)
            yearTV = view.findViewById(R.id.row_result_yearTV)
            dayTV = view.findViewById(R.id.row_result_dayTV)
            infoTV = view.findViewById(R.id.row_result_infoTV)
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

        viewHolder.infoTV.text = "${nowDate.selectedNum}명 / ${resultAct.userNum}명"
    }

    fun addData(tmp : dateClass){
        dataSet.add(tmp)
        notifyItemInserted(dataSet.size - 1)
    }

    override fun getItemCount() = dataSet.size

}