package com.polidea.cockpit.paramsedition.viewholder

import android.view.View

internal class StepDoubleParamViewHolder(view: View) : StepParamViewHolder<Double>(view) {
    override fun doubleToValue(double: Double): Double = double
}