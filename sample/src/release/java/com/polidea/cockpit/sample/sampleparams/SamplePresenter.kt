package com.polidea.cockpit.sample.sampleparams

class SamplePresenter(override val sampleView: SampleContract.View)
    : SampleBasePresenter(sampleView), SampleContract.Presenter {

    init {
        sampleView.presenter = this
    }
}