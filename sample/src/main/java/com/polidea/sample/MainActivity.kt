package com.polidea.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.sample.util.initViews
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        displayCurrentCockpit()
    }

    private fun displayCurrentCockpit() {
        val params = Cockpit.getAllCockpitParams()
        val builder = StringBuilder().append("\n")

        for (p in params) {
            builder.append("${p.name}: ${p.value}\n")
        }

        (cockpit_textview as TextView).append(builder.toString())
    }
}

