package com.polidea.androidtweaks.manager

import android.content.Context


class TweaksManager private constructor(context: Context) {
//    val params: List<Param<*>> = YamlParser().parseYaml("tweaks.yml", context)
//
//    companion object {
//        @Volatile
//        private var INSTANCE: TweaksManager? = null
//
//        @JvmStatic
//        fun getInstance(context: Context): TweaksManager {
//            if (INSTANCE == null) {
//                INSTANCE = TweaksManager(context)
//            }
//
//            return INSTANCE as TweaksManager
//        }
//    }
//
//    fun <T> getParamValue(key: String): T? = getParam<T>(key)?.value
//
//    fun <T> setParamValue(key: String, value: T) {
//        val param = getParam<T>(key)
//        param ?: throw IllegalArgumentException("Param with name $key undefined!")
//        param.value = value
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    private fun <T> getParam(key: String): Param<T>? = params.find { it.name == key } as Param<T>?
}