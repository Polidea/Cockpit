package com.polidea.cockpit.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.polidea.cockpit.R
import com.polidea.cockpit.exception.CockpitFormatException
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.utils.FileUtils
import com.polidea.cockpit.utils.ViewUtils
import com.polidea.cockpit.view.ParamView
import kotlinx.android.synthetic.debug.cockpit_activity_layout.*

class CockpitActivity : AppCompatActivity() {

    val TAG = CockpitActivity::class.java.simpleName

    var params = CockpitManager.getInstance().params

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cockpit)
        initializeSaveButton()
    }

    private fun initializeSaveButton() {
        save_button.setOnClickListener{
            saveCockpit()
        }
    }

    private fun saveCockpit() {
        val cockpitViews: ArrayList<ParamView<*>> = ViewUtils().getFlatChildren(cockpit_view)

        cockpitViews.forEach { view ->
            params.find {
                it.name == view.paramName
            }?.also {
                        try {
                            CockpitManager.getInstance().setParamValue(it.name, view.getCurrentValue())
                        } catch (e: CockpitFormatException) {
                            Toast.makeText(this, "Invalid param value for: ${view.paramName}", Toast.LENGTH_SHORT).show()
                            Log.w(TAG, "Invalid param value for: ${view.paramName}")
                            return
                        }
                    }
        }

        FileUtils(this).saveCockpitAsYaml()

        finish()
    }
}

