package com.polidea.cockpit.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import com.polidea.cockpit.R
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

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, resources.getDimension(R.dimen.cockpit_margin).toInt())

        params.map {
            val type = it.typeClass
            when (type) {
                Double::class.javaObjectType -> addView(DoubleParamView(context, null, 0, it.name, it.value as Double), layoutParams)
                Int::class.javaObjectType -> addView(IntParamView(context, null, 0, it.name, it.value as Int), layoutParams)
                String::class.java -> addView(TextParamView(context, null, 0, it.name, it.value as String), layoutParams)
                Boolean::class.javaObjectType -> addView(BooleanParamView(context, null, 0, it.name, it.value as Boolean), layoutParams)
            }
        }
    }
}