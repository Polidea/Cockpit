package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.BasePresenter
import com.polidea.cockpit.BaseView

internal interface ParamsEditionContract {

    interface View : BaseView<Presenter>, ParamView {
        fun expand()

        fun collapse()

        fun resize(height: Int)

        fun dismiss()

        fun showColorPicker(itemPosition: ItemPosition, color: Int)
    }

    interface ParamView {
        fun reloadParam(itemPosition: ItemPosition)

        fun reloadAll()
    }

    interface Presenter : BasePresenter {

        fun getParamsModel(): ParamsModel

        fun <T : Any> onParamChange(itemPosition: ItemPosition, newValue: T)

        fun <T : Any> onParamValueSelected(itemPosition: ItemPosition, selectedItemIndex: Int)

        fun requestAction(itemPosition: ItemPosition)

        fun restore(itemPosition: ItemPosition)

        fun restoreAll()

        fun expand()

        fun resize(height: Int)

        fun collapse()

        fun hidden()

        fun editColor(itemPosition: ItemPosition)

        fun newColorSelected(itemPosition: ItemPosition, color: Int)
    }
}
