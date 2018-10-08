package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.polidea.cockpit.R

internal class GroupViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val separator: View = view.findViewById(R.id.cockpit_group_separator)
    private val name: TextView = view.findViewById(R.id.cockpit_group_name)

    fun display(groupName: String?) {
        if (adapterPosition == 0)
            separator.visibility = View.GONE
        else
            separator.visibility = View.VISIBLE

        name.text = groupName ?: view.context.getString(R.string.default_group_name)
    }
}