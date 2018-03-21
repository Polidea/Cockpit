package com.polidea.sample.util

import android.view.View
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.sample.MainActivity
import com.polidea.sample.R

internal fun MainActivity.initViews() {
    findViewById<View>(R.id.edit_values_button).setOnClickListener {
        onEditValues()
    }
}

private fun MainActivity.onEditValues() {
    Cockpit.showCockpit(this)
}
