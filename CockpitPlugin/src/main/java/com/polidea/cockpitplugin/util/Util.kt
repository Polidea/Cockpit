package com.polidea.cockpitplugin.util

internal class Util {

    companion object {
        // Kotlin version of following deep merge map method:
        // http://blog.mathieu.photography/post/103163278870/deep-merge-map-in-groovy
        fun <K : Any?, V : Any?> deepMerge(onto: MutableMap<K, V>, vararg overrides: Map<K, V>): MutableMap<K, V> {
            if (overrides.isEmpty()) {
                return onto
            } else if (overrides.size == 1) {
                overrides[0].forEach { k, v ->
                    if (v is MutableMap<*, *> && onto[k] is Map<*, *>) {
                        deepMerge(onto[k] as MutableMap<K, V>, v as MutableMap<K, V>)
                    } else {
                        onto[k] = v
                    }
                }
                return onto
            }
            return overrides.inject(onto) { acc, override ->
                if (override.isEmpty()) {
                    deepMerge(acc, mapOf())
                } else {
                    deepMerge(acc, override)
                }
            }
        }

        // equivalent of Groovy's method Array.inject(...)
        private fun <E, T, U : T, V : T> Array<E>.inject(initialValue: U, closure: (T, E) -> V): T {
            var value: T = initialValue
            val length = this.size

            for (index in 0 until length) {
                val next = this[index]
                value = closure(value, next)
            }
            return value
        }
    }
}