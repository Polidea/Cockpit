package com.polidea.cockpit.paramsedition

import android.graphics.Color
import com.polidea.cockpit.core.CockpitParamGroup
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.paramsedition.refactor.DisplayModel
import com.polidea.cockpit.paramsedition.refactor.toDisplayModel
import com.polidea.cockpit.utils.colorToArgbHexString
import java.util.*


internal class CockpitDialogPresenter(private val view: ParamsEditionContract.View) : ParamsEditionContract.Presenter {

    init {
        view.presenter = this
    }

    private val model: ParamsModel = ParamsEditionModel()
    private val displayStack: Stack<DisplayModel> = Stack()

    override fun start() {
        displayModel(model.topLevelGroups.toDisplayModel())
    }

    private fun displayModel(displayModel: DisplayModel) {
        displayStack.push(displayModel)
        view.display(displayStack.peek())
    }

    override fun stop() {
        model.save()
    }

    override fun restore(paramName: String) {
        model.restoreValue(paramName)
        view.reloadParam(paramName)
    }

    override fun restoreAll() {
        model.restoreAll()
        displayStack.clear()
        displayModel(model.topLevelGroups.toDisplayModel())
    }

    override fun onDisplayGroup(group: CockpitParamGroup) {
        displayModel(group.toDisplayModel())
    }

    override fun <T : Any> onParamChange(paramName: String, newValue: T) {
        model.setValue(paramName, newValue)
    }

    override fun <T : Any> onParamValueSelected(paramName: String, selectedItemIndex: Int) {
        model.selectValue<T>(paramName, selectedItemIndex)
    }

    override fun requestAction(paramName: String) {
        model.requestAction(paramName)
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

    override fun requestResize(height: Int) {
        view.resize(height)
    }

    override fun editColor(paramName: String) {
        val param = model.getParam<CockpitColor>(paramName)
        view.showColorPicker(paramName, Color.parseColor(param.value.value))
    }

    override fun newColorSelected(paramName: String, color: Int) {
        onParamChange(paramName, CockpitColor(colorToArgbHexString(color)))
        view.reloadParam(paramName)
    }
}
