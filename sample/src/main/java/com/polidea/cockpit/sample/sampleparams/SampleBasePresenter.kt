package com.polidea.cockpit.sample.sampleparams

import android.graphics.Color
import android.graphics.Typeface
import com.polidea.cockpit.cockpit.Cockpit

abstract class SampleBasePresenter(open val sampleView: SampleBaseContract.View<*>) : SampleBaseContract.Presenter {

    override fun start() {
        initViews()
    }

    override fun stop() {
    }

    protected open fun initViews() {
        sampleView.setTotalPriceFontSize(Cockpit.getTotalPriceFontSize().toFloat())
        sampleView.setHeadingText(Cockpit.getHeadingText())
        sampleView.setFooterText(Cockpit.getFooter())
        sampleView.setFooterTextColor(Color.parseColor(Cockpit.getFooterFontColor()))
        sampleView.showFooter(true)
    }
}