package com.polidea.cockpit.paramsedition.viewholder

import android.support.v7.widget.AppCompatSpinner
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.polidea.cockpit.R
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitListType

internal class ListParamViewHolder(view: View) : SelectionParamBaseViewHolder<CockpitListType<Any>>(view), AdapterView.OnItemSelectedListener {

    private val itemsSpinner: AppCompatSpinner = view.findViewById(R.id.cockpit_list_param_value)

    override fun displayParam(param: CockpitParam<CockpitListType<Any>>) {
        super.displayParam(param)
        val cockpitList = param.value
        val arrayAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, cockpitList.items)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemsSpinner.adapter = arrayAdapter
        itemsSpinner.onItemSelectedListener = null
        itemsSpinner.setSelection(cockpitList.selectedIndex)
        itemsSpinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        valueSelectedListener?.invoke(itemsSpinner.selectedItemPosition)
    }
}