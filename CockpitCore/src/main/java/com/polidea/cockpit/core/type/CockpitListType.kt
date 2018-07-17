package com.polidea.cockpit.core.type


data class CockpitListType<T: Any>(val items: List<T>, var selectedIndex: Int)