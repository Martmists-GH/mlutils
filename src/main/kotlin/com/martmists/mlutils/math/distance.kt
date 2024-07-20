package com.martmists.mlutils.math

import com.martmists.ndarray.simd.F64FlatArray
import kotlin.math.absoluteValue
import kotlin.math.sqrt

/**
 * Calculates the cosine distance between this vector and some [other] vector.
 */
fun F64FlatArray.cosineDistance(other: F64FlatArray): Double {
    return (this.dot(other) / (this.norm() * other.norm()))
}

/**
 * Calculates the l1 (manhattan) distance between this vector and some [other] vector.
 */
fun F64FlatArray.l1Distance(other: F64FlatArray): Double {
    return (this - other).transform { it.absoluteValue }.sum()
}

/**
 * Calculates the l2 (euclidean) distance between this vector and some [other] vector.
 */
fun F64FlatArray.l2Distance(other: F64FlatArray): Double {
    return sqrt((this - other).transform { it * it }.sum())
}
