fun Any.isSimpleType() =
        when (this) {
            is Number -> true
            is Boolean -> true
            is String -> true
            else -> false
        }