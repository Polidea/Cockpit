package com.polidea.cockpit.paramsedition

data class GroupSize(val subgroupsSize: Int, val paramsSize: Int) {
    val size = subgroupsSize + paramsSize
}
