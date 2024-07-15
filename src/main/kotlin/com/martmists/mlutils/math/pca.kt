package com.martmists.mlutils.math

import com.martmists.mlutils.compat.jvm.ofRows
import com.martmists.mlutils.compat.jvm.rows
import org.jetbrains.bio.viktor.F64Array
import org.jetbrains.bio.viktor.F64FlatArray
import kotlin.math.sqrt

/**
 * Reduces dimensionality of `points` to `k` dimensions using PCA.
 *
 * @param k The number of dimensions to reduce to.
 * @param points The points to reduce.
 * @return The reduced points.
 */
fun pca(k: Int, points: List<F64FlatArray>): List<F64FlatArray> {

    val mean = points.reduce { acc, array -> acc + array } / points.size.toDouble()
    val stdDev = (points.reduce { acc, array -> acc + (array - mean).let { it * it } } / (points.size - 1.0)).transform(::sqrt)
    val normalized = F64Array.ofRows(points.map { (it - mean) / stdDev } )

    val asMatrix = F64Array.ofRows(points)
    val covMatrix = asMatrix.transpose().matmul(asMatrix) / (points.size - 1.0)
    val (eigenValues, eigenVectors) = covMatrix.eigen()
    val values = eigenValues.toDoubleArray()
    val vectors = eigenVectors.rows()

    val sorted = values.mapIndexed { index, d -> index to d }.sortedByDescending(Pair<Int, Double>::second).map(Pair<Int, Double>::first)
    val sortedVectors = sorted.map(vectors::get)

    val topEigen = sortedVectors.take(k)
    val newMat = F64Array.ofRows(topEigen)

    return normalized.matmul(newMat.transpose()).rows()
}
