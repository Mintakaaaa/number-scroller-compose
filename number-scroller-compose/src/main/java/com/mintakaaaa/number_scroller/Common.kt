package com.mintakaaaa.number_scroller


/**
 * Gets the number of decimal places in a float value based on the step size.
 *
 * @param value The float value for which to get the number of decimal places.
 * @return The number of decimal places in the value.
 */
fun getDecimalPlaces(value: Float): Int {
    val valueString = value.toString()
    return if (valueString.contains('.')) {
        valueString.substringAfter('.').length
    } else 0
}

/**
 * Truncates trailing zeros from a string representation of a number.
 *
 * @param x The string representation of the number.
 * @return The number with trailing zeros removed.
 */
fun truncateTrailingZeros(x: String): Number {
    val result = x.toBigDecimal().stripTrailingZeros()
    return if (result.scale() <= 0) result.toInt() else result.toFloat()
}
