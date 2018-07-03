package com.polidea.cockpit.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.polidea.cockpit.Dismissable
import com.polidea.cockpit.R
import com.polidea.cockpit.paramsedition.ParamsEditionFragment
import com.polidea.cockpit.paramsedition.ParamsEditionPresenter
import com.polidea.cockpit.utils.replaceFragmentInActivity

class CockpitActivity : AppCompatActivity(), Dismissable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cockpit_activity)

        val paramsEditionFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as ParamsEditionFragment? ?: ParamsEditionFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

        ParamsEditionPresenter(paramsEditionFragment)
    }

    override fun dismiss() {
        finish()
    }
}

