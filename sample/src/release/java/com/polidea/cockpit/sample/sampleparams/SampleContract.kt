package com.polidea.cockpit.sample.sampleparams

interface SampleContract {

    interface View : SampleBaseContract.View<Presenter>

    interface Presenter : SampleBaseContract.Presenter
}