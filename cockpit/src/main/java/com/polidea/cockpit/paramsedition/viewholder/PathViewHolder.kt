package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.polidea.cockpit.R

internal class PathViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var onPathClickedListener: () -> Unit = {}
    private val button: View = view.findViewById(R.id.path_button)

    init {
        button.setOnClickListener {
            onPathClickedListener()
        }
    }
}