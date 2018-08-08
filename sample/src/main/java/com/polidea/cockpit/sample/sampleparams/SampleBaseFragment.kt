package com.polidea.cockpit.sample.sampleparams

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polidea.cockpit.sample.R
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

abstract class SampleBaseFragment<T : SampleBaseContract.Presenter> : Fragment(), SampleBaseContract.View<T> {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_shopping_cart, container, false)


    override fun showFooter(isVisible: Boolean) {
        footer_container.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    override fun setFooterText(footerText: String) {
        footer_text_view.text = footerText
    }

    override fun setFooterTextColor(color: Int) {
        footer_text_view.setTextColor(color)
    }

    override fun setTotalPriceFontSize(textSize: Float) {
        total_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
    }

    override fun setHeadingText(headingText: String) {
        heading_text_view.text = headingText
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