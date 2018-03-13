package com.polidea.androidtweaks.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.polidea.androidtweaks.R
import com.polidea.androidtweaks.exception.TweakFormatException
import com.polidea.androidtweaks.manager.TweaksManager
import com.polidea.androidtweaks.utils.FileUtils
import com.polidea.androidtweaks.utils.ViewUtils
import com.polidea.androidtweaks.view.ParamView
import kotlinx.android.synthetic.main.tweaks_activity_layout.*

class TweaksActivity : AppCompatActivity() {

    val TAG = TweaksActivity::class.java.simpleName

    var params = TweaksManager.getInstance().params

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweaks)
        initializeSaveButton()
    }

    private fun initializeSaveButton() {
        save_button.setOnClickListener{
            saveTweaks()
        }
    }

    private fun saveTweaks() {
        val tweakViews: ArrayList<ParamView<*>> = ViewUtils().getFlatChildren(tweaks_view)

        tweakViews.forEach { view ->
            params.find {
                it.name == view.paramName
            }?.also {
                        try {
                            TweaksManager.getInstance().setParamValue(it.name, view.getCurrentValue())
                        } catch (e: TweakFormatException) {
                            Toast.makeText(this, "Invalid tweak value for: ${view.paramName}", Toast.LENGTH_SHORT).show()
                            Log.w(TAG, "Invalid tweak value for: ${view.paramName}")
                            return
                        }
                    }
        }

        FileUtils(this).saveTweaksAsYaml()

        finish()
    }
}

