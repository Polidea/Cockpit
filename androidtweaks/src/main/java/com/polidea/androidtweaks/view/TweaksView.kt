package com.polidea.androidtweaks.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.polidea.androidtweaks.manager.TweaksManager


class TweaksView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        initialize()
    }

    private fun initialize() {
        val params = TweaksManager.getInstance().params

        orientation = VERTICAL

        params.map {
            when (it.typeClass) {
                Double::class -> addView(DoubleParamView(context, null, 0, it.name, it.value as Double))
                Int::class -> addView(IntParamView(context, null, 0, it.name, it.value as Int))
                String::class -> addView(TextParamView(context, null, 0, it.name, it.value as String))
                Boolean::class -> addView(BooleanParamView(context, null, 0, it.name, it.value as Boolean))
            }
        }
    }
}