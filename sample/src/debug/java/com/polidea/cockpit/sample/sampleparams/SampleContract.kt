package com.polidea.cockpit.sample.sampleparams

interface SampleContract {

    interface View : SampleBaseContract.View<Presenter> {
        fun showCockpitUi()

        fun showMessage(message: String)
    }

    interface Presenter : SampleBaseContract.Presenter {
        fun shakeDetected()
    }
}
