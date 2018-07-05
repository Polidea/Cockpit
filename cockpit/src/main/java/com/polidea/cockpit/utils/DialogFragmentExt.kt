package com.polidea.cockpit.utils

import android.support.v7.app.AppCompatDialogFragment
import android.view.WindowManager

internal fun AppCompatDialogFragment.removeDimmedBackground() {
    val window = dialog.window
    val windowParams = window.attributes
    windowParams.flags = windowParams.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
    window.attributes = windowParams
}