package com.polidea.sample.util

import android.view.View
import com.polidea.androidtweaks.tweaks.Tweaks
import com.polidea.sample.MainActivity
import com.polidea.sample.R

internal fun MainActivity.initViews() {
    findViewById<View>(R.id.edit_values_button).setOnClickListener {
        onEditValues()
    }
}

private fun MainActivity.onEditValues() {
    Tweaks.showTweaks(this)
}
