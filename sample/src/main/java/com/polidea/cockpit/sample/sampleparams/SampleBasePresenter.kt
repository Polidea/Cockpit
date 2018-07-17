package com.polidea.cockpit.sample.sampleparams

import android.graphics.Color
import android.util.Log
import com.polidea.cockpit.cockpit.Cockpit

abstract class SampleBasePresenter(open val sampleView: SampleBaseContract.View<*>) : SampleBaseContract.Presenter {

    override fun start() {
        initViews()
    }

    override fun stop() {
    }

    protected open fun initViews() {
        sampleView.setColorDescription(Cockpit.getColorDescription())
        sampleView.setFooterText(Cockpit.getFooter())
        sampleView.setFontSize(Cockpit.getFontSize().toFloat())
        sampleView.showFooter(true)
        val color = Cockpit.getColor()
        try {
            sampleView.setTextColor(Color.parseColor(color))
        } catch (e: IllegalArgumentException) {
            Log.w(TAG, "Unable to parse $color color")
        }
    }

    companion object {
        val TAG: String = SampleBasePresenter::class.java.simpleName
    }
}