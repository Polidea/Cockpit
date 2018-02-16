package com.polidea.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.polidea.androidtweaks.TweaksActivity
import com.polidea.androidtweaks.tweaks.Tweaks


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, TweaksActivity::class.java)
        startActivity(intent)

        //todo remove, test
        System.out.println(Tweaks.param_tag1)
        Tweaks.param_tag1 = 666.0
        System.out.println(Tweaks.param_tag1)
    }
}

