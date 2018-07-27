package com.polidea.cockpit.paramsedition.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.polidea.cockpit.R

class ColorPreviewView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    fun showColor(@ColorInt color: Int) {
        val drawable: GradientDrawable = getDrawable(R.drawable.color_preview) as GradientDrawable
        drawable.mutate()
        drawable.setColor(color)
        background = drawable
    }

    fun showPlaceholder() {
        background = getDrawable(R.drawable.v_color_placeholder)
    }

    private fun getDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(context, id)
}