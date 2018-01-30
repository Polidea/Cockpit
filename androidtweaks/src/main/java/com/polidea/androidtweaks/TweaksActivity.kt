package com.polidea.androidtweaks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class TweaksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweaks)

//        val settingsManager = TweaksManager.getInstance(this.applicationContext)
//        System.out.println(settingsManager.params)
//        settingsManager.setParamValue("param_tag1", 1.4)
//        System.out.println(settingsManager.params)
//        TweaksGenerator().generate(TweaksManager.getInstance(this.applicationContext).params)
    }
}
