package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.BasePresenter
import com.polidea.cockpit.BaseView
import com.polidea.cockpit.core.CockpitParamGroup
import com.polidea.cockpit.paramsedition.refactor.DisplayModel

internal interface ParamsEditionContract {

    interface View : BaseView<Presenter>, ParamView {
        fun expand()

        fun collapse()

        fun resize(height: Int)

        fun dismiss()

        fun showColorPicker(paramName: String, color: Int)
    }

    interface ParamView {
        fun reloadParam(paramName: String)

        fun reloadAll()

        fun display(model: DisplayModel)
    }

    interface Presenter : BasePresenter {

        fun <T : Any> onParamChange(paramName: String, newValue: T)

        fun <T : Any> onParamValueSelected(paramName: String, selectedItemIndex: Int)

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
