package com.polidea.cockpit.extensions

import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment

internal fun AppCompatDialogFragment.removeDimmedBackground() {
    dialog?.window?.let {
        val windowParams = it.attributes
        windowParams.flags = windowParams.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        it.attributes = windowParams
    }
}