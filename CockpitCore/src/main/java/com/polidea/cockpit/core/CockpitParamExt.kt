package com.polidea.cockpit.core


fun <T : Any> CockpitParam<T>.isSimpleParam(): Boolean = isValueSimpleType() && !hasOptionalFields()

private fun <T : Any> CockpitParam<T>.isValueSimpleType() = when (value) {
    is Number -> true
    is Boolean -> true
    is String -> true
    else -> false
}

private fun <T : Any> CockpitParam<T>.hasOptionalFields() = description != null || group != null