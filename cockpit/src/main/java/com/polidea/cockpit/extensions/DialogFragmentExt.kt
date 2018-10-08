package com.polidea.cockpit.extensions

import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment

internal fun AppCompatDialogFragment.removeDimmedBackground() {
    val window = dialog.window
    val windowParams = window.attributes
    windowParams.flags = windowParams.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
    window.attributes = windowParams
}