package com.martmists.mlutils.compat.jvm

import org.jetbrains.bio.viktor.F64Array
import org.jetbrains.bio.viktor.F64FlatArray

/**
 * Creates an [F64Array] from a [List] of [DoubleArray].
 */
fun F64Array.Companion.ofRows(args: List<DoubleArray>): F64Array {
    require(args.isNotEmpty()) { "Cannot create F64Array from empty list" }
    require(args[0].isNotEmpty()) { "Cannot create F64Array from empty array" }
    require(args.all { it.size == args[0].size }) { "Cannot create F64Array from arrays of different sizes" }
    return F64Array(args.size, args[0].size) { i, j -> args[i][j] }
}

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
 * Creates an [F64Array] from a [List] of [F64FlatArray].
 */
@JvmName("ofRowsF64FlatArray")
fun F64Array.Companion.ofRows(args: List<F64FlatArray>): F64Array {
    require(args.isNotEmpty()) { "Cannot create F64Array from empty list" }
    require(args[0].length != 0) { "Cannot create F64Array from empty array" }
    require(args.all { it.length == args[0].length }) { "Cannot create F64Array from arrays of different sizes" }
    return F64Array(args.size, args[0].length) { i, j -> args[i][j] }
}

/**
 * Extracts rows from a 2D [F64Array].
 *
 * @return a [List] of [F64FlatArray], one for each row.
 */
fun F64Array.rows(): List<F64FlatArray> {
    require(shape.size == 2) { "Cannot convert F64Array of shape ${this.shape} to rows" }
    return (0 until shape[0]).map { this.view(it).flatten() }
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

/**
 * Creates a 2D identity [F64Array] of size [size].
 *
 * @param size The size of the identity matrix.
 * @return The identity matrix.
 */
fun F64Array.Companion.identity(size: Int): F64Array {
    val arr = F64Array.full(size, size, init=0.0)
    for (i in 0 until size) {
        arr[i, i] = 1.0
    }
    return arr
}


/**
 * Creates a 2D [F64Array] with [values] as the diagonal.
 *
 * @param values The [F64FlatArray] to put on the diagonal.
 * @return The diagonal matrix.
 */
fun F64Array.Companion.diagonal(values: F64FlatArray): F64Array {
    val size = values.length
    val arr = F64Array.full(size, size, init=0.0)
    for (i in 0 until size) {
        arr[i, i] = values[i]
    }
    return arr
}
