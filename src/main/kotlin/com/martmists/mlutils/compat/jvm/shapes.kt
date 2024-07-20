package com.martmists.mlutils.compat.jvm

import com.martmists.ndarray.simd.F64Array
import com.martmists.ndarray.simd.F64FlatArray

/**
 * Creates an [F64Array] from a [List] of [FloatArray].
 */
@JvmName("ofRowsFloat")
fun F64Array.Companion.ofRows(args: List<FloatArray>): F64Array {
    require(args.isNotEmpty()) { "Cannot create F64Array from empty list" }
    require(args[0].isNotEmpty()) { "Cannot create F64Array from empty array" }
    require(args.all { it.size == args[0].size }) { "Cannot create F64Array from arrays of different sizes" }
    return F64Array(args.size, args[0].size) { i, j -> args[i][j].toDouble() }
}

/**
 * Extracts pixels from a 3D [F64Array].
 *
 * @return a [List] of [F64FlatArray], one for each pixel.
 */
fun F64Array.pixels(): List<F64FlatArray> {
    require(shape.size == 3) { "Cannot convert F64Array of shape ${this.shape} to pixels" }
    val xRange = 0 until shape[0]
    val yRange = 0 until shape[1]
    return (xRange.flatMap { x -> yRange.map { y -> this.V[x, y] } }).map { it.flatten() }
}
