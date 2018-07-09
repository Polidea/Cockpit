package com.polidea.cockpit.sample.sampleparams

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.sample.R
import kotlinx.android.synthetic.main.fragment_sample.*

class SampleFragment : SampleBaseFragment<SampleContract.Presenter>(), SampleContract.View {

    override lateinit var presenter: SampleContract.Presenter

    private val debugDescriptionView: TextView by lazy {
        variant_include_container.findViewById<TextView>(R.id.cockpit_debug_textview)
    }

    override fun setDebugDescription(description: String) {
        debugDescriptionView.text = description
    }

    override fun showCockpitUi() {
        Cockpit.showCockpit(fragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        variant_include_container.findViewById<Button>(R.id.edit_values_button)
                .setOnClickListener { _ -> presenter.editValues() }
    }

    companion object {
        fun newInstance() =
                SampleFragment()
    }
}
