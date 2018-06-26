package com.polidea.cockpit.sample.sampleparams

interface SampleContract {

    interface View : SampleBaseContract.View<Presenter> {
        fun setDebugDescription(description: String)

        fun showCockpitUi()
    }

    interface Presenter : SampleBaseContract.Presenter {
        fun editValues()
    }
}
