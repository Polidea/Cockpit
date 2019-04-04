package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.BasePresenter
import com.polidea.cockpit.BaseView
import com.polidea.cockpit.core.CockpitParamGroup

internal interface ParamsEditionContract {

    interface View : BaseView<Presenter>, ParamView {
        fun expand()

        fun collapse()

        fun resize(height: Int)

        fun dismiss()

        fun showColorPicker(paramName: String, color: Int)

        fun displayNavigationDialog(options: List<String>)
    }

    interface ParamView {
        fun reloadParam(paramName: String)

        fun reloadAll()

        fun display(model: DisplayModel)
    }

    interface Presenter : BasePresenter {

        fun <T : Any> onParamChange(paramName: String, newValue: T)

        fun <T : Any> onParamValueSelected(paramName: String, selectedItemIndex: Int)

        fun onPathClicked()

        fun onNavigateBack()

        fun onNavigateToTop()

        fun onNavigateToCrumb(crumb: String)

        fun onDisplayGroup(group: CockpitParamGroup)

        fun requestAction(paramName: String)

        fun restore(paramName: String)

        fun restoreAll()

        fun expand()

        fun requestResize(height: Int)

        fun collapse()

        fun hidden()

        fun editColor(paramName: String)

        fun newColorSelected(paramName: String, color: Int)
    }
}
