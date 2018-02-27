package com.polidea.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.polidea.androidtweaks.tweaks.Tweaks
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edit_values_button.setOnClickListener {
            onEditValues()
        }
    }

    override fun onResume() {
        super.onResume()
        displayCurrentTweaks()
    }

    private fun displayCurrentTweaks() {
        val params = Tweaks.getAllTweaks()
        val builder = StringBuilder().append("\n")

        for (p in params) {
            builder.append("${p.name}: ${p.value}\n")
        }

        (tweaks_textview as TextView).append(builder.toString())
    }

    private fun onEditValues() {
        Tweaks.showTweaks(this)
    }
}

