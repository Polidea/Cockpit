package com.polidea.cockpit.event;

public interface PropertyChangeListener<T> {
    void onValueChange(T oldValue, T newValue);
}
