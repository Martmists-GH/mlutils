package com.martmists.mlutils.compat.jvm

import com.martmists.ndarray.simd.F64Array
import com.martmists.ndarray.simd.F64FlatArray

/**
 * Creates an [F64Array] from a [FloatArray].
 */
fun F64Array.Companion.of(array: FloatArray) = F64Array(array.size) { array[it].toDouble() }.flatten()

/**
 * Converts a [F64FlatArray] to a [FloatArray].
 */
fun F64FlatArray.toFloatArray() = this.toDoubleArray().map { it.toFloat() }.toFloatArray()
