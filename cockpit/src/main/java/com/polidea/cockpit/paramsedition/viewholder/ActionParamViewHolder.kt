package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import android.widget.Button
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam

class ActionParamViewHolder(view: View): ParamBaseViewHolder<String>(view) {

    var actionButtonClickListener: (() -> Unit)? = null

    private val actionButton: Button = view.findViewById(R.id.cockpit_action_button)

    init {
        actionButton.setOnClickListener { actionButtonClickListener?.invoke() }
    }

    override fun displayParam(param: CockpitParam<String>) {
        super.displayParam(param)
        actionButton.text = param.value
    }
}