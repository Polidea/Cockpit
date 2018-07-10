package com.polidea.cockpit.manager

import com.polidea.cockpit.event.ActionCallback

class CallbackNotifier {

    private val callbacks: MutableMap<String, MutableSet<ActionCallback>> = mutableMapOf()

    fun add(paramName: String, callback: ActionCallback) {
        val paramListeners = getCallbacks(paramName)
        paramListeners.add(callback)
        callbacks[paramName] = paramListeners
    }

    fun remove(paramName: String, callback: ActionCallback) {
        callbacks[paramName]?.remove(callback)
    }

    fun firePerformAction(paramName: String) {
        val paramListeners = getCallbacks(paramName)
        paramListeners.forEach {
            it.onActionPerform(paramName)
        }
    }

    private fun getCallbacks(name: String): MutableSet<ActionCallback> = callbacks[name] ?: mutableSetOf()
}