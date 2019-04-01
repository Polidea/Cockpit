package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.polidea.cockpit.R

internal class GroupViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val name: TextView = view.findViewById(R.id.subgroup_name)

    fun display(groupName: String?) {
        name.text = groupName ?: view.context.getString(R.string.default_group_name)
        view.setOnClickListener { Toast.makeText(view.context, groupName, Toast.LENGTH_SHORT).show() }
    }
}