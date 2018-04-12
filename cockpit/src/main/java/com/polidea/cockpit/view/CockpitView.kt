package com.polidea.cockpit.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.polidea.cockpit.manager.CockpitManager


class CockpitView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        initialize()
    }

    private fun initialize() {
        val params = CockpitManager.params

        orientation = VERTICAL

        params.map {
            val type = it.typeClass
            when (type) {
                Double::class.javaObjectType -> addView(DoubleParamView(context, null, 0, it.name, it.value as Double))
                Int::class.javaObjectType -> addView(IntParamView(context, null, 0, it.name, it.value as Int))
                String::class.java -> addView(TextParamView(context, null, 0, it.name, it.value as String))
                Boolean::class.javaObjectType -> addView(BooleanParamView(context, null, 0, it.name, it.value as Boolean))
            }
        }
    }
}