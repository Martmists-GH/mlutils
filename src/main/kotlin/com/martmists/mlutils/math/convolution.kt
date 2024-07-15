package com.martmists.mlutils.math

import org.jetbrains.bio.viktor.F64Array

/**
 * Convolves this array with some [kernel].
 *
 * @param kernel The kernel to convolve with.
 * @return The convolved array, with the same shape as this array.
 */
fun F64Array.convolve(kernel: F64Array): F64Array {
    require(shape.size == 2 || shape.size == 3) { "Expected 2D or 3D array, got ${shape.size}D" }
    require(kernel.shape.size == 2) { "Expected 2D kernel, got ${kernel.shape.size}D" }

    val kernelSize = kernel.shape[0]
    val kernelCenter = kernelSize / 2

    val result = if (shape.size == 2) {
        F64Array(shape[0], shape[1]) { x, y ->
            var sum = 0.0
            for (i in 0 until kernelSize) {
                for (j in 0 until kernelSize) {
                    val xIndex = x + i - kernelCenter
                    val yIndex = y + j - kernelCenter
                    if (xIndex in 0 until shape[0] && yIndex in 0 until shape[1]) {
                        sum += this[xIndex, yIndex] * kernel[i, j]
                    }
                }
            }
            sum
        }
    } else {
        F64Array(shape[0], shape[1], shape[2]) { x, y, z ->
            var sum = 0.0
            for (i in 0 until kernelSize) {
                for (j in 0 until kernelSize) {
                    val xIndex = x + i - kernelCenter
                    val yIndex = y + j - kernelCenter
                    if (xIndex in 0 until shape[0] && yIndex in 0 until shape[1]) {
                        sum += this[xIndex, yIndex, z] * kernel[i, j]
                    }
                }
            }
            sum
        }
    }
    return result
}
