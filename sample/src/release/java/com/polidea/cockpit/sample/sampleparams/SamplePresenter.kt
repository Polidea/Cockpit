package com.polidea.cockpit.sample.sampleparams

import com.polidea.cockpit.sample.model.SampleModel

class SamplePresenter(override val sampleView: SampleContract.View, override val sampleModel: SampleModel)
    : SampleBasePresenter(sampleView, sampleModel), SampleContract.Presenter {

    init {
        sampleView.presenter = this
    }
}