package com.polidea.cockpit.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.sample.util.initViews
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
        val builder = StringBuilder().append("\n")

        builder.append("param_tag1: ${Cockpit.getparam_tag1()}\n")
        builder.append("param_tag2: ${Cockpit.getparam_tag2()}\n")
        builder.append("param_tag3: ${Cockpit.getparam_tag3()}\n")
        builder.append("int_param: ${Cockpit.getint_param()}\n")
        builder.append("boolean_param: ${Cockpit.getboolean_param()}\n")

        (cockpit_textview as TextView).append(builder.toString())
    }
}

