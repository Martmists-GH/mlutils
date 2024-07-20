package com.martmists.mlutils.convert

import com.martmists.ndarray.simd.F64Array
import com.martmists.ndarray.simd.F64FlatArray


/**
 * Converts an [F64FlatArray] from RGB/RGBA to HSV.
 *
 * @receiver an [F64FlatArray] with three or four values, each must be in the range 0..1
 * @return an [F64FlatArray] with three values, Hue in range 0..360, Saturation and Value in range 0..1
 */
fun F64FlatArray.rgbToHsv(): F64FlatArray {
    // H in [0 .. 360]
    // S in [0 .. 1]
    // V in [0 .. 1]
    var (r, g, b) = this.toDoubleArray()

    r *= 255
    g *= 255
    b *= 255

    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min

    var h = when {
        delta == 0.0 -> 0.0
        max == r -> 60.0 * ((g - b) / delta % 6)
        max == g -> 60.0 * ((b - r) / delta + 2)
        else -> 60.0 * ((r - g) / delta + 4)
    }

    if (h < 0.0) h += 360.0

    val s = if (max == 0.0) 0.0 else delta / max
    val v = max / 255.0

    return F64Array.of(h, s, v).flatten()
}

/**
 * Converts an [F64FlatArray] from HSV to RGB.
 *
 * @receiver an [F64FlatArray] with three values, Hue in range 0..360, Saturation and Value in range 0..1
 * @return an [F64FlatArray] with three values, each in the range 0..1
 */
fun F64FlatArray.hsvToRgb(): F64FlatArray {
    val (h, s, v) = this.toDoubleArray()

    fun component(n: Int): Double {
        val k = (n + h / 60) % 6
        return v - (v * s * maxOf(0.0, minOf(k, 4 - k, 1.0)))
    }

    val r = component(5)
    val g = component(3)
    val b = component(1)

    return F64Array.of(r, g, b).flatten()
}

