package com.polidea.cockpit.exception

class UnsupportedCockpitTypeException(paramName: String, clazz: Class<*>) :
        Exception("Unsupported ${clazz.canonicalName} type for $paramName param")