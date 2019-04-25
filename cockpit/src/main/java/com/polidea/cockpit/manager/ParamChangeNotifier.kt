package com.polidea.cockpit.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.polidea.cockpit.event.PropertyChangeListener

internal class ParamChangeNotifier {

    private val listeners: MutableMap<String, MutableSet<*>> = mutableMapOf()

    fun <T : Any> add(paramName: String, listener: PropertyChangeListener<T>) {
        val paramListeners = getListeners<MutableSet<LifecycleAwareListener<T>>>(paramName)
        paramListeners.add(LifecycleAwareListener(null, listener, paramName))
        listeners[paramName] = paramListeners
    }

    fun <T : Any> add(lifecycleOwner: LifecycleOwner, paramName: String, listener: PropertyChangeListener<T>) {
        val paramListeners = getListeners<MutableSet<LifecycleAwareListener<T>>>(paramName)
        paramListeners.add(LifecycleAwareListener(lifecycleOwner, listener, paramName))
        listeners[paramName] = paramListeners
    }

    fun <T : Any> remove(paramName: String, listener: PropertyChangeListener<T>) {
        val toRemove = listeners[paramName]?.find { (it as LifecycleAwareListener<T>).listener == listener }
        listeners[paramName]?.remove(toRemove)
    }

    fun <T : Any> firePropertyChange(paramName: String, oldValue: T, newValue: T) {
        if (oldValue == newValue)
            return

        val paramListeners = getListeners<MutableSet<LifecycleAwareListener<T>>>(paramName)
        paramListeners.forEach {
            it.listener.onValueChange(oldValue, newValue)
        }
    }

    private inline fun <reified T : Any> getListeners(name: String): T =
            (listeners[name] ?: mutableSetOf<T>()).let { it as T }

    private inner class LifecycleAwareListener<T : Any>(val lifecycleOwner: LifecycleOwner?,
                                                        val listener: PropertyChangeListener<T>,
                                                        val paramName: String) : LifecycleObserver {
        init {
            lifecycleOwner?.lifecycle?.addObserver(this)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            lifecycleOwner?.lifecycle?.removeObserver(this)
            remove(paramName, listener)
        }
    }
}

