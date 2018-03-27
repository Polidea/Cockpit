package com.polidea.cockpit.sample.util

import android.view.View
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.sample.MainActivity
import com.polidea.cockpit.sample.R

internal fun MainActivity.initViews() {
    findViewById<View>(R.id.edit_values_button).setOnClickListener {
        onEditValues()
    }
}

private fun MainActivity.onEditValues() {
    Cockpit.showCockpit(this)
}
