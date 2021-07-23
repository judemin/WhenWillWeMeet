package com.mong.whenwillwemeet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class chatAdapter(private val selDay: SelectDayActivity) :
    RecyclerView.Adapter<chatAdapter.ViewHolder>() {
    private val dataSet: Vector<msgClass> = Vector()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var leftLL : LinearLayout
        var leftNameTV: TextView
        var leftContextTV: TextView

        var rightLL : LinearLayout
        var rightContextTV: TextView

        init {
            leftLL = view.findViewById(R.id.row_chat_leftll)
            leftNameTV = view.findViewById(R.id.row_chat_left_nameTV)
            leftContextTV = view.findViewById(R.id.row_chat_left_contextTV)

            rightLL = view.findViewById(R.id.row_chat_rightll)
            rightContextTV = view.findViewById(R.id.row_chat_right_contextTV)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_chat, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val nowMsg : msgClass = dataSet[position]

        if(nowMsg.senderCode.equals(selDay.nowUser.pid)){ // 전송자가 본인일 경우
            viewHolder.leftNameTV.isGone = true
            viewHolder.leftLL.isInvisible = true

            viewHolder.rightContextTV.text = nowMsg.msg
        } else{ // 전송자가 상대일 경우
            viewHolder.rightLL.isInvisible = true

            viewHolder.leftNameTV.text = nowMsg.sender
            viewHolder.leftContextTV.text = nowMsg.msg
        }
    }

    fun addData(tmp : msgClass){
        dataSet.add(tmp)
        notifyItemInserted(dataSet.size - 1)
    }

    override fun getItemCount() = dataSet.size

}