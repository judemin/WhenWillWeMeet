package com.mong.whenwillwemeet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class calendarAdapter(private val dataSet: Array<Calendar>) : // dateClass를 Calendar로 변환하여 넘겨줌
    RecyclerView.Adapter<calendarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var buttons: Array<Button>

        init {
            buttons[0] = view.findViewById(R.id.row_cal_btn1)
            buttons[1] = view.findViewById(R.id.row_cal_btn2)
            buttons[2] = view.findViewById(R.id.row_cal_btn3)
            buttons[3] = view.findViewById(R.id.row_cal_btn4)
            buttons[4] = view.findViewById(R.id.row_cal_btn5)
            buttons[5] = view.findViewById(R.id.row_cal_btn6)
            buttons[6] = view.findViewById(R.id.row_cal_btn7)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_calendar, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.textView.text = dataSet[position]
    }

    override fun getItemCount() = dataSet.size

}