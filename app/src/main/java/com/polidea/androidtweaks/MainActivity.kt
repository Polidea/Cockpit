package com.polidea.androidtweaks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.polidea.androidtweaks.manager.SettingsManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        System.out.println(SettingsManager(this.applicationContext).params)
    }
}
