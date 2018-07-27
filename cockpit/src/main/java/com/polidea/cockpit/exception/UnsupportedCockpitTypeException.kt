package com.polidea.cockpit.exception

internal class UnsupportedCockpitTypeException(paramName: String, clazz: Class<*>) :
        Exception("Unsupported ${clazz.canonicalName} type for $paramName param")
// TODO [PU] You may consider adding a reference to documentation that will show what kind of types are supported.