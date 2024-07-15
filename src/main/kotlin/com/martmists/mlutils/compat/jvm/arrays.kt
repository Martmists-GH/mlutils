package com.martmists.mlutils.compat.jvm

import org.jetbrains.bio.viktor.F64Array
import org.jetbrains.bio.viktor.F64FlatArray

/**
 * Creates an [F64Array] from a [DoubleArray].
 */
fun F64Array.Companion.of(array: DoubleArray) = F64Array(array.size) { array[it] }.flatten()

/**
 * Creates an [F64Array] from a [FloatArray].
 */
fun F64Array.Companion.of(array: FloatArray) = F64Array(array.size) { array[it].toDouble() }.flatten()

/**
 * Converts a [F64FlatArray] to a [FloatArray].
 */
fun F64FlatArray.toFloatArray() = this.toArray().map { it.toFloat() }.toFloatArray()
