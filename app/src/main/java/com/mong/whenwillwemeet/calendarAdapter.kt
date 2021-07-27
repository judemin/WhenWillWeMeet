package com.mong.whenwillwemeet

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class calendarAdapter(private val selDay: SelectDayActivity) : // 2차원 월 데이터
    RecyclerView.Adapter<calendarAdapter.ViewHolder>() {

    val dataSet: Vector<dateClass> = Vector()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linLayout : LinearLayout = view.findViewById(R.id.row_cal_ll)
        var yearTV: TextView = view.findViewById(R.id.row_cal_yearTV)
        var dayTV: TextView = view.findViewById(R.id.row_cal_dayTV)
        var checkBox: CheckBox = view.findViewById(R.id.row_cal_checkBox)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_calendar, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val nowDay : dateClass = dataSet[position]
        val state : Boolean = selDay.nowUser.selectedDates.contains(nowDay.makeKey())
        // Rec View는 스크롤마다 뷰를 재사용하기 때문에 기존 뷰에 관한 데이터를 저장하고 있어야 함
        // 그래서 스크롤하고 올라오면 컨텐츠의 내용이 기존의 내용으로 대체됨
        // position이 반복되어 뷰 내의 상태로 나머지를 컨트롤하면 안됨

        viewHolder.yearTV.setText("${nowDay.year}년 ${nowDay.month + 1}월")
        viewHolder.dayTV.setText("${nowDay.day}일 (${dateClass.dayofweek[nowDay.dayOfWeek - 1]})")

        if(state) {
            viewHolder.checkBox.isChecked = true
            viewHolder.checkBox.buttonTintList = getColorStateList(
                selDay.applicationContext,
                R.color.baseBlue
            )
        } else {
            viewHolder.checkBox.isChecked = false
            viewHolder.checkBox.buttonTintList = getColorStateList(
                selDay.applicationContext,
                R.color.pastelRed
            )
        }

        viewHolder.itemView.setOnClickListener {
            selDay.onClickDate(nowDay, state)
            clickEvent(viewHolder,position,nowDay)
        }
    }

    private fun clickEvent(viewHolder: ViewHolder, position: Int, nowDay : dateClass){
        val checkB = viewHolder.checkBox
        val state : Boolean = checkB.isChecked

        checkB.performClick()
        if(!state) {
            checkB.buttonTintList = getColorStateList(
                selDay.applicationContext,
                R.color.baseBlue
            )
        } else {
            checkB.buttonTintList = getColorStateList(
                selDay.applicationContext,
                R.color.pastelRed
            )
        }
    }

    fun addData(tmp: dateClass){
        dataSet.add(tmp)
        notifyItemInserted(dataSet.size - 1)
    }

    override fun getItemCount() = dataSet.size

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

}