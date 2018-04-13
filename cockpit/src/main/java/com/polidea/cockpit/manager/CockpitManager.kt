package com.polidea.cockpit.manager

object CockpitManager {

    val params: MutableList<CockpitParam> = ArrayList()

    fun addParam(param: CockpitParam) {
        checkIfExistsAndAddParam(param)
    }

    private fun checkIfExistsAndAddParam(param: CockpitParam) {
        if (!exists(param.name)) {
            System.out.println("Param ${param.name} doesn't exist, adding")
            params.add(param)
        } else {
            System.out.println("Param ${param.name} already exists")
        }
    }

    fun getParamValue(name: String): Any? = getParam(name)?.value

    fun setParamValue(key: String, value: Any) {
        val param = getParam(key)
        param ?: throw IllegalArgumentException("Param with name $key undefined!")
        param.value = value
    }

    fun exists(key: String): Boolean {
        return params.find { it.name == key } != null
    }

    fun clear() {
        params.clear()
    }

    private fun getParam(name: String): CockpitParam? = params.find { it.name == name }
}