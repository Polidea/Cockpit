package com.polidea.cockpit.sample.sampleparams

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polidea.cockpit.sample.R
import kotlinx.android.synthetic.main.fragment_sample.*

abstract class SampleBaseFragment<T : SampleBaseContract.Presenter> : Fragment(), SampleBaseContract.View<T> {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_sample, container, false)


    override fun showFooter(isVisible: Boolean) {
        footer_container.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    override fun setTextColor(color: Int) {
        cockpit_textview.setTextColor(color)
        cockpit_color_textview.setTextColor(color)
    }

    override fun setFontSize(textSize: Float) {
        cockpit_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
    }

    override fun setColorDescription(description: String) {
        cockpit_color_textview.text = description
    }

    override fun setFooterText(footerText: String) {
        footer_text_view.text = footerText
    }

    override fun setTypeface(typeface: Typeface) {
        cockpit_textview.typeface = typeface
        Log.d("SampleBaseFragment", "typeface: ${cockpit_textview.typeface.style}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }
}