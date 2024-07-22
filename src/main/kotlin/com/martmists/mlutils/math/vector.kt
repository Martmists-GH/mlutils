package com.martmists.mlutils.math

import com.martmists.ndarray.simd.F64Array
import com.martmists.ndarray.simd.F64Array.Companion.zeros

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
        val offDiagonal = (tmp - F64Array.diagonal(tmp.diagonal().toDoubleArray())).flatten().norm()
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
        val x = r.slice(i, shape[0], axis=0).view(i, axis=1).flatten()
        val e = zeros(shape[0] - i)
        e[0] = x.norm()
        val v = (x - e).let { it / it.norm() }
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
