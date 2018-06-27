package com.polidea.cockpit.sample.sampleparams

class SampleFragment : SampleBaseFragment<SampleContract.Presenter>(), SampleContract.View {

    override lateinit var presenter: SampleContract.Presenter

    companion object {
        fun newInstance() =
                SampleFragment()
    }
}