package com.polidea.cockpit.paramsedition

import android.graphics.Color
import com.polidea.cockpit.core.CockpitParamGroup
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.utils.colorToArgbHexString
import java.util.Stack


internal class CockpitDialogPresenter(private val view: ParamsEditionContract.View) : ParamsEditionContract.Presenter {

    init {
        view.presenter = this
    }

    private val model: ParamsModel = ParamsEditionModel()
    private val displayStack: Stack<DisplayModel> = Stack()

    override fun start() {
        restoreDisplayState()
    }

    private fun restoreDisplayState() {
        displayStack.push(model.topLevelGroups.toDisplayModel())
        NavigationState.getHierarchy().forEach {
            val nextDisplayModel = model.getGroup(it).toDisplayModel(displayStack.peek().breadcrumb, true)
            displayStack.push(nextDisplayModel)
        }
        displayCurrentModel()
    }

    private fun displayNewModel(displayModel: DisplayModel) {
        displayStack.push(displayModel)
        displayCurrentModel()
    }

    private fun displayCurrentModel() {
        val model = displayStack.peek()
        view.display(model)
        NavigationState.saveHierarchy(model.breadcrumb.crumbs)
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
        restoreDisplayState()
    }

    override fun onPathClicked() {
        view.displayNavigationDialog(listOf()) //TODO
    }

    override fun onNavigateBack() {
        displayStack.pop()
        displayCurrentModel()
    }

    override fun onNavigateToTop() {
        displayStack.clear()
        displayNewModel(model.topLevelGroups.toDisplayModel())
    }

    override fun onNavigateToCrumb(crumb: String) {
        val index = NavigationState.getHierarchy().lastIndexOf(crumb)
        val toPop = NavigationState.getHierarchy().lastIndex - index

        if (index > 0 && toPop > 0) {
            for (i in 0..toPop) {
                displayStack.pop()
            }

            displayCurrentModel()
        }
    }

    override fun onDisplayGroup(group: CockpitParamGroup) {
        displayNewModel(group.toDisplayModel(displayStack.peek().breadcrumb, true))
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
