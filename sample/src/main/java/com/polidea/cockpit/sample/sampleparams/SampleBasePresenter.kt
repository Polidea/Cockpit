package com.polidea.cockpit.sample.sampleparams

import android.graphics.Color
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.sample.Style

abstract class SampleBasePresenter(open val sampleView: SampleBaseContract.View<*>) : SampleBaseContract.Presenter {

    override fun start() {
        initViews()
    }

    override fun stop() {
    }

    protected open fun initViews() {
        sampleView.setStyle(Style.forValue(Cockpit.getStyleSelectedValue()))
        sampleView.setTotalPriceFontSize(Cockpit.getTotalPriceFontSize().toFloat())
        sampleView.setHeadingText(Cockpit.getHeadingText())
        sampleView.setFooterText(Cockpit.getFooter())
        sampleView.setFooterTextColor(Color.parseColor(Cockpit.getFooterFontColor()))
        sampleView.showFooter(true)
    }
}