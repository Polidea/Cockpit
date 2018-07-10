package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam


class CockpitDialogPresenter(private val view: ParamsEditionContract.View) : ParamsEditionContract.Presenter {

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

    override fun <T : Any> getParamAt(position: Int): CockpitParam<T> = model.getParamAt(position)

    override fun getParamsSize() = model.size

    override fun restore(position: Int) {
        model.restoreValue(position)
        view.reloadParam(position)
    }

    override fun restoreAll() {
        model.restoreAll()
        view.reloadAll()
    }

    override fun <T : Any> onParamChange(position: Int, newValue: T) {
        model.setValue(position, newValue)
    }

    override fun performAction(position: Int) {
        model.performAction(position)
    }

    override fun expand() {
        view.expand()
    }

    override fun collapse() {
        view.collapse()
    }
}
