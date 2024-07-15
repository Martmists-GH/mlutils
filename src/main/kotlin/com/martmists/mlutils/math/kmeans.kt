package com.martmists.mlutils.math

import org.jetbrains.bio.viktor.F64Array
import org.jetbrains.bio.viktor.F64FlatArray

typealias Cluster = List<F64FlatArray>

/**
 * Cluster the `points` into `k` clusters using K-means clustering.
 *
 * @param k The number of clusters
 * @param points The points to cluster
 * @return A map of size `k` from centroid to cluster
 */
fun clusterKMeans(k: Int, points: List<F64FlatArray>): Map<F64FlatArray, Cluster> {
    require(points.size >= k) { "Not enough points to cluster" }
    require(k > 0) { "k must be positive" }
    val distinct = points.distinct().size
    require(distinct >= k) { "Not enough distinct points to cluster" }

    // TODO: Warn if low number of distinct values? Might loop for large amount of time

    if (k == 1) return mapOf(points[0] to points)
    if (points.size == k) return points.associateWith { listOf(it) }

    var centroids = points.shuffled().take(k)
    val clusters = mutableListOf<MutableList<F64FlatArray>>()

    var converged = false
    while (!converged) {
        clusters.clear()
        repeat(k) {
            clusters.add(mutableListOf())
        }

        for (p in points) {
            val closest = centroids.minBy { it.l2Distance(p) }
            clusters[centroids.indexOf(closest)].add(p)
        }

        val idealCentroids = clusters.map { (it.fold(F64Array.full(centroids[0].length, 0.0)) { acc, array -> acc + array } / it.size.toDouble()).flatten() }
        val closestCentroids = clusters.withIndex().map { (i, cluster) -> cluster.minByOrNull { it.l2Distance(idealCentroids[i]) } ?: points.random() }

        if (centroids.zip(closestCentroids).all { (a, b) -> a === b }) {
            converged = true
        } else {
            centroids = closestCentroids.toMutableList()
        }
    }

    return centroids.zip(clusters).associate { (k, v) -> k to v }
}

/**
 * Cluster the `points` into `k` clusters using K-means clustering.
 *
 * @param k The number of clusters
 * @param points The points to cluster
 * @return A map of size `k` from centroid to cluster
 */
fun clusterKMeans(k: Int, points: Array<F64FlatArray>): Map<F64FlatArray, Cluster> {
    return clusterKMeans(k, points.toList())
}

/**
 * Cluster the `points` into `k` clusters using K-means clustering.
 *
 * @param k The number of clusters
 * @param points The points to cluster
 * @return A map of size `k` from centroid to cluster
 */
@JvmName("clusterND")
fun clusterKMeans(k: Int, points: List<F64Array>): Map<F64FlatArray, Cluster> {
    return clusterKMeans(k, points.map { it.flatten() })
}

/**
 * Cluster the `points` into `k` clusters using K-means clustering.
 *
 * @param k The number of clusters
 * @param points The points to cluster
 * @return A map of size `k` from centroid to cluster
 */
fun clusterKMeans(k: Int, points: Array<F64Array>): Map<F64FlatArray, Cluster> {
    return clusterKMeans(k, points.map { it.flatten() })
}