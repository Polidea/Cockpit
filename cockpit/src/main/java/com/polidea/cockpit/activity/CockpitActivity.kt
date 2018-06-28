package com.polidea.cockpit.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.polidea.cockpit.R
import com.polidea.cockpit.exception.CockpitFormatException
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.utils.FileUtils
import com.polidea.cockpit.utils.ViewUtils
import com.polidea.cockpit.view.*
import kotlinx.android.synthetic.main.cockpit_include_activity_layout.*

class CockpitActivity : AppCompatActivity() {

    private val TAG = CockpitActivity::class.java.simpleName

    private var params = CockpitManager.params
    private var defaultParams = CockpitManager.defaultParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cockpit_activity)

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, resources.getDimension(R.dimen.cockpit_margin).toInt())

        params.forEach {
            val key = it.name
            val value = it.value
            val description = it.description
            val group = it.group
            val paramView: View? = when (value) {
                is Double -> DoubleParamView(this, null, 0, key, description, group).also { configureView(it, value) }
                is Int -> IntParamView(this, null, 0, key, description, group).also { configureView(it, value) }
                is String -> TextParamView(this, null, 0, key, description, group).also { configureView(it, value) }
                is Boolean -> BooleanParamView(this, null, 0, key, description, group).also { configureView(it, value) }
                else -> null
            }
            paramView?.let { cockpit_view.addView(it, layoutParams) }
        }

        cockpit_restore_defaults_button.setOnClickListener { restoreDefaults() }
        cockpit_save_button.setOnClickListener { saveCockpit() }
    }

    private fun <T : Any> configureView(view: ParamView<T>, value: T) {
        view.value = value
        val defaultValue = defaultValue<T>(view.paramName)
        view.getRestoreButton().setOnClickListener { view.value = defaultValue }
    }

    private fun restoreDefaults() {
        val cockpitViews = ViewUtils.getFlatChildren(cockpit_view)
        cockpitViews.forEach { paramView ->
            val paramName = paramView.paramName
            when (paramView) {
                is DoubleParamView -> paramView.value = defaultValue(paramName)
                is IntParamView -> paramView.value = defaultValue(paramName)
                is TextParamView -> paramView.value = defaultValue(paramName)
                is BooleanParamView -> paramView.value = defaultValue(paramName)
            }
        }
    }

    private fun <T> defaultValue(paramName: String): T {
        return defaultParams.find { it.name == paramName }?.value as? T
                ?: throw IllegalStateException("Not found default value for $paramName parameter")
    }


    private fun saveCockpit() {
        val cockpitViews = ViewUtils.getFlatChildren(cockpit_view)
        cockpitViews.forEach { view ->
            params.find {
                it.name == view.paramName
            }?.also {
                try {
                    CockpitManager.setParamValue(it.name, view.value)
                } catch (e: CockpitFormatException) {
                    Toast.makeText(this, "Invalid param value for: ${view.paramName}", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "Invalid param value for: ${view.paramName}")
                    return
                }
            }
        }

        FileUtils.saveCockpitAsYaml()

        finish()
    }
}

