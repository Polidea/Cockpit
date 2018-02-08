package com.polidea.androidtweaks.manager

import kotlin.reflect.KClass


data class TweakParam(val name: String, val typeClass: KClass<*>, var value: Any)