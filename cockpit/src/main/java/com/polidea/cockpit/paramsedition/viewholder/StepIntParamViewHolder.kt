package com.polidea.cockpit.paramsedition.viewholder

import android.view.View

internal class StepIntParamViewHolder(view: View) : StepParamViewHolder<Int>(view) {
    override fun doubleToValue(double: Double): Int = double.toInt()
}