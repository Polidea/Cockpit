package com.polidea.cockpit.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.polidea.cockpit.event.ActionRequestCallback

internal class CallbackNotifier {

    private val callbacks: MutableMap<String, MutableSet<LifecycleAwareCallback>> = mutableMapOf()

    fun add(paramName: String, callback: ActionRequestCallback) {
        val paramListeners = getCallbacks(paramName)
        paramListeners.add(LifecycleAwareCallback(null, callback, paramName))
        callbacks[paramName] = paramListeners
    }

    fun add(lifecycleOwner: LifecycleOwner, paramName: String, callback: ActionRequestCallback) {
        val paramListeners = getCallbacks(paramName)
        paramListeners.add(LifecycleAwareCallback(lifecycleOwner, callback, paramName))
        callbacks[paramName] = paramListeners
    }

    fun remove(paramName: String, callback: ActionRequestCallback) {
        val toRemove = callbacks[paramName]?.find { it.callback == callback }
        callbacks[paramName]?.remove(toRemove)
    }

    fun requestAction(paramName: String) {
        val paramListeners = getCallbacks(paramName)
        paramListeners.forEach {
            it.callback.onActionRequested()
        }
    }

    private fun getCallbacks(name: String): MutableSet<LifecycleAwareCallback> = callbacks[name] ?: mutableSetOf()

    private inner class LifecycleAwareCallback(private val lifecycleOwner: LifecycleOwner?,
                                               val callback: ActionRequestCallback,
                                               private val paramName: String) : LifecycleObserver {
        init {
            lifecycleOwner?.lifecycle?.addObserver(this)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            lifecycleOwner?.lifecycle?.removeObserver(this)
            remove(paramName, callback)
        }
    }
}

