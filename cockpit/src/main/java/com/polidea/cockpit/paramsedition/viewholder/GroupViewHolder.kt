package com.polidea.cockpit.paramsedition.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.polidea.cockpit.R

internal class GroupViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.cockpit_group_name)

    fun display(groupName: String) {
        name.text = groupName
    }
}