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
import com.polidea.cockpit.persistency.CockpitYamlFileManager
import com.polidea.cockpit.utils.FileUtils
import com.polidea.cockpit.utils.ViewUtils
import com.polidea.cockpit.view.*
import kotlinx.android.synthetic.main.cockpit_include_activity_layout.*
import java.util.*

class CockpitActivity : AppCompatActivity() {

    private val TAG = CockpitActivity::class.java.simpleName

    private var params = CockpitManager.params

    private lateinit var defaultParams: Map<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cockpitYamlFileManager = CockpitYamlFileManager(filesDir.path, assets)
        defaultParams = Collections.unmodifiableMap(cockpitYamlFileManager.readInputParams())

        setContentView(R.layout.cockpit_activity)

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, resources.getDimension(R.dimen.cockpit_margin).toInt())

        params.forEach {
            val key = it.name
            val value = it.value
            val paramView: View? = when (value) {
                is Double -> DoubleParamView(this, null, 0, key).also { configureView(it, value, defaultParams[it.paramName] as Double) }
                is Int -> IntParamView(this, null, 0, key).also { configureView(it, value, defaultParams[it.paramName] as Int) }
                is String -> TextParamView(this, null, 0, key).also { configureView(it, value, defaultParams[it.paramName] as String) }
                is Boolean -> BooleanParamView(this, null, 0, key).also { configureView(it, value, defaultParams[it.paramName] as Boolean) }
                else -> null
            }
            paramView?.let { cockpit_view.addView(it, layoutParams) }
        }

        cockpit_restore_defaults_button.setOnClickListener { restoreDefaults() }
        cockpit_save_button.setOnClickListener { saveCockpit() }
    }

    private fun <T : Any> configureView(view: ParamView<T>, value: T, defaultValue: T) {
        view.value = value
        view.getRestoreButton().setOnClickListener { view.value = defaultValue }
    }

    private fun restoreDefaults() {
        val cockpitViews = ViewUtils.getFlatChildren(cockpit_view)
        cockpitViews.forEach {
            val defaultValue = defaultParams[it.paramName]
            val view = it
            when (view) {
                is DoubleParamView -> view.value = defaultValue as Double
                is IntParamView -> view.value = defaultValue as Int
                is TextParamView -> view.value = defaultValue as String
                is BooleanParamView -> view.value = defaultValue as Boolean
            }
        }
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

