package com.polidea.cockpit.sample.util

import android.view.View
import android.widget.TextView
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.sample.MainActivity
import com.polidea.cockpit.sample.R

internal fun MainActivity.initViews() {
    findViewById<View>(R.id.edit_values_button).setOnClickListener {
        onEditValues()
    }
    findViewById<TextView>(R.id.cockpit_debug_textview).text = Cockpit.getDebugDescription()
}

private fun MainActivity.onEditValues() {
    Cockpit.showCockpit(this)
}
