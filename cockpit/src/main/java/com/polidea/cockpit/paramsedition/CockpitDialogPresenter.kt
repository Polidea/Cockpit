package com.polidea.cockpit.paramsedition

import android.graphics.Color
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.utils.colorToArgbHexString


internal class CockpitDialogPresenter(private val view: ParamsEditionContract.View) : ParamsEditionContract.Presenter {

    init {
        view.presenter = this
    }

    private val model = ParamsEditionModel()

    override fun start() {
        view.reloadAll()
    }

    override fun stop() {
        model.save()
    }

    override fun getParamsModel() = model

    override fun restore(itemPosition: ItemPosition) {
        model.restoreValue(itemPosition)
        view.reloadParam(itemPosition)
    }

    override fun restoreAll() {
        model.restoreAll()
        view.reloadAll()
    }

    override fun <T : Any> onParamChange(itemPosition: ItemPosition, newValue: T) {
        model.setValue(itemPosition, newValue)
    }

    override fun <T : Any> onParamValueSelected(itemPosition: ItemPosition, selectedItemIndex: Int) {
        model.selectValue<T>(itemPosition, selectedItemIndex)
    }

    override fun requestAction(itemPosition: ItemPosition) {
        model.requestAction(itemPosition)
    }

    override fun expand() {
        view.expand()
    }

    override fun collapse() {
        view.collapse()
    }

    override fun hidden() {
        view.dismiss()
    }

    override fun editColor(itemPosition: ItemPosition) {
        val param = model.getParamAt<CockpitColor>(itemPosition)
        view.showColorPicker(itemPosition, Color.parseColor(param.value.value))
    }

    override fun newColorSelected(itemPosition: ItemPosition, color: Int) {
        onParamChange(itemPosition, CockpitColor(colorToArgbHexString(color)))
        view.reloadParam(itemPosition)
    }
}
