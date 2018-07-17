package com.polidea.cockpit.paramsedition.viewholder

import android.support.v7.widget.AppCompatSpinner
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitListType

internal class ListParamViewHolder(view: View) : SelectionParamBaseViewHolder<CockpitListType<*>>(view), AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        valueSelectedListener?.invoke(itemsSpinner.selectedItemPosition)
    }

    private val itemsSpinner: AppCompatSpinner = view.findViewById(R.id.cockpit_list_param_value)

    init {
        itemsSpinner.onItemSelectedListener = this
    }

    override fun displayParam(param: CockpitParam<CockpitListType<*>>) {
        super.displayParam(param)
        val cockpitList = param.value
        val arrayAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, cockpitList.items)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemsSpinner.adapter = arrayAdapter
        itemsSpinner.setSelection(cockpitList.selectedIndex)
    }
}