package com.mong.whenwillwemeet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class calendarAdapter(private val dataSet: Array<Array<Int>>) : // 2차원 월 데이터
    RecyclerView.Adapter<calendarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var buttons: Array<Button>

        init {
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_calendar, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(dataSet[position] == null){
            viewHolder.buttons[position].isVisible = false
            viewHolder.buttons[position].isEnabled = false
        } else {
            viewHolder.buttons[position].setText(dataSet[position].get(Calendar.DAY_OF_MONTH))
        }
    }

    override fun getItemCount() = dataSet.size

}