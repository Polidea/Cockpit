package com.polidea.cockpit.paramsedition.viewholder

import android.view.View

internal class RangeIntParamViewHolder(view: View) : RangeParamViewHolder<Int>(view) {

    override fun doubleToValue(double: Double) = double.toInt()
}