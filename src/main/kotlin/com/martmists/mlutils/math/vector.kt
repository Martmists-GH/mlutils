package com.martmists.mlutils.math

import com.martmists.ndarray.simd.F64Array
import com.martmists.ndarray.simd.F64FlatArray
import kotlin.math.sqrt

/**
 * Calculates the norm of a vector.
 */
fun F64Array.norm(): Double {
    val sum = (this * this).sum()
    return sqrt(sum)
}

/**
 * Returns a transposed array. Only supports 1D and 2D arrays.
 */
fun F64Array.transpose(): F64Array {
    return when (shape.size) {
        1 -> F64Array(shape[0], 1) { i, j -> this[i] }
        2 -> F64Array(shape[1], shape[0]) { i, j -> this[j, i] }
        else -> throw IllegalArgumentException("Only 1D and 2D arrays are supported")
    }
}

/**
 * Calculates the outer product of this vector and some [other] vector.
 */
fun F64FlatArray.outer(other: F64FlatArray): F64Array {
    return F64Array(shape[0], other.shape[0]) { i, j -> this[i] * other[j] }
}

/**
 * Calculates the eigenvalues and eigenvectors of this [F64Array] using the QR algorithm.
 */
fun F64Array.eigen(): Pair<F64Array, F64Array> {
    require(shape.size == 2) { "Only 2D arrays are supported" }
    require(shape[0] == shape[1]) { "Only square matrices are supported" }

    var tmp = this.copy()
    val tolerance = 1e-10
    val iterations = 1000

    var total = F64Array.identity(shape[0])

    for (i in 0 until iterations) {
        val (q, r) = tmp.qr()
        tmp = r.matmul(q)
        total = total.matmul(q)
        val offDiagonal = (tmp - F64Array.diagonal(tmp.diagonal().toDoubleArray())).norm()
        if (offDiagonal < tolerance) {
            break
        }
    }

    val eigenvalues = tmp.diagonal()
    val eigenvectors = total

    return eigenvalues to eigenvectors
}

private fun F64Array.qr(): Pair<F64Array, F64Array> {
    var q = F64Array.identity(shape[0])
    var r = this.copy()

    for (i in 0 until shape[0] - 1) {
        val x = r.slice(i, shape[0], axis=0).view(i, axis=1)
        val e = F64Array.zeros(intArrayOf(shape[0] - i))
        e[0] = x.norm()
        val v = (x - e).let { it / it.norm() }.flatten()
        val qi = F64Array.identity(shape[0])
        val delta = v.outer(v) * 2.0

        for (a in i until shape[0]) {
            for (b in i until shape[0]) {
                qi[a, b] -= delta[a - i, b - i]
            }
        }

        r = qi.matmul(r)
        q = q.matmul(qi)
    }

    return q to r
}
