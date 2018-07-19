package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.BasePresenter
import com.polidea.cockpit.BaseView
import com.polidea.cockpit.core.CockpitParam

internal interface ParamsEditionContract {

    interface View : BaseView<Presenter>, ParamView {
        fun expand()

        fun collapse()

        fun dismiss()
    }

    interface ParamView {
        fun reloadParam(position: Int)

        fun reloadAll()
    }

    interface Presenter : BasePresenter {

        fun <T : Any> getParamAt(position: Int): CockpitParam<T>

        fun getParamsSize(): Int

        fun <T : Any> onParamChange(position: Int, newValue: T)

        fun <T : Any> onParamValueSelected(position: Int, selectedItemIndex: Int)

        fun requestAction(position: Int)

        fun restore(position: Int)

        fun restoreAll()

        fun expand()

        fun collapse()

        fun hidden()
    }
}
