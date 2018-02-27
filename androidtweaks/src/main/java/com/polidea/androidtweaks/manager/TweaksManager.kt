package com.polidea.androidtweaks.manager

class TweaksManager private constructor() {

    var params: MutableList<TweakParam> = ArrayList()

    companion object {
        @Volatile
        private var INSTANCE: TweaksManager? = null

        @JvmStatic
        fun getInstance(): TweaksManager {
            if (INSTANCE == null) {
                INSTANCE = TweaksManager()
            }

            return INSTANCE as TweaksManager
        }
    }

    fun addParam(param: TweakParam) {
        checkIfExistsAndAddParam(param)
    }

    private fun checkIfExistsAndAddParam(param: TweakParam) {
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

    private fun getParam(name: String): TweakParam? = params.find { it.name == name }
}