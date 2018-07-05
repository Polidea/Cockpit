package com.polidea.cockpit.type.core


data class CockpitListType<T: Any>(val items: List<T>, var selectedIndex: Int) {

}