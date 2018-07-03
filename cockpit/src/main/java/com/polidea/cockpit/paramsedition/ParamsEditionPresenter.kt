package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam


class ParamsEditionPresenter(private val view: ParamsEditionContract.View) : ParamsEditionContract.Presenter {

    private val model = ParamsEditionModel()

    init {
        view.presenter = this
    }

    override fun start() {
        view.reloadAll()
    }

    override fun stop() {
    }

    override fun <T : Any> getParamAt(position: Int): CockpitParam<T> = model.getParamAt(position)

    override fun getParamsSize() = model.size

    override fun <T : Any> onParamChange(position: Int, newValue: T) {
        model.setValue(position, newValue)
    }

    override fun restore(position: Int) {
        model.restoreValue(position)
        view.reloadParam(position)
    }

    override fun restoreAll() {
        model.restoreAll()
        view.reloadAll()
    }

    override fun save() {
        model.save()
        view.dismiss()
    }
}
