package com.polidea.cockpit.paramsedition

object NavigationState {
    private var navHierarchy: List<String> = listOf()

    fun saveHierarchy(hierarchy: List<String>) {
        navHierarchy = hierarchy
    }

    fun getHierarchy(): List<String> = navHierarchy
}