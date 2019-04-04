package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class PathViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var onPathClickedListener: () -> Unit = {}

    init {
        view.setOnClickListener { onPathClickedListener() }
    }
}