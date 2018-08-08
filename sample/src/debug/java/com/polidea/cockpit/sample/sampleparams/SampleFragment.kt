package com.polidea.cockpit.sample.sampleparams

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.sample.R
import com.squareup.seismic.ShakeDetector
import kotlinx.android.synthetic.main.fragment_sample.*

class SampleFragment : SampleBaseFragment<SampleContract.Presenter>(), SampleContract.View, ShakeDetector.Listener {
    override lateinit var presenter: SampleContract.Presenter

    private val debugDescriptionView: TextView by lazy {
        variant_include_container.findViewById<TextView>(R.id.cockpit_debug_textview)
    }

    private val shakeDetector = ShakeDetector(this)

    override fun hearShake() {
        presenter.shakeDetected()
    }

    private fun setShakeDetector() {
        val sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector.start(sensorManager)
    }

    override fun setDebugDescription(description: String) {
        debugDescriptionView.text = description
    }

    override fun showCockpitUi() {
        Cockpit.showCockpit(fragmentManager)
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setShakeDetector()
    }

    override fun onStop() {
        super.onStop()
        shakeDetector.stop()
    }

    companion object {
        fun newInstance() =
                SampleFragment()
    }
}
