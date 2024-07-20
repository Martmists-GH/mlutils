package com.martmists.mlutils.math

import com.martmists.ndarray.simd.F64Array
import com.martmists.ndarray.simd.F64FlatArray
import com.martmists.ndarray.simd._I
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private fun Int.reverse(): Int {
    var v = this
    v = (v and 0x55555555 shl 1) or (v ushr 1 and 0x55555555)
    v = (v and 0x33333333 shl 2) or (v ushr 2 and 0x33333333)
    v = (v and 0x0f0f0f0f shl 4) or (v ushr 4 and 0x0f0f0f0f)
    v = (v shl 24) or (v and 0xff00 shl 8) or (v ushr 8 and 0xff00) or (v ushr 24)
    return v
}

/**
 * Performs a Fast Fourier Transform on this array.
 * The output is not normalized.
 *
 * @receiver The 1D real array to perform the FFT on.
 * @return The 1D Complex FFT of this array.
 */
fun F64FlatArray.fftRealToComplex1D(): F64Array {
    val out = F64Array(length, 2)
    out.V[_I, 0] = this
    out.V[_I, 1] = 0.0

    return out.fftComplexToComplex1D()
}

/**
 * Performs a Fast Fourier Transform on this array.
 * The output is not normalized.
 *
 * @receiver The 1D Complex array to perform the FFT on.
 * @return The 1D Complex FFT of this array.
 */
fun F64Array.fftComplexToComplex1D(): F64Array {
    require(shape.size == 2) { "Only 1D arrays are supported" }
    require(shape[1] == 2) { "Only complex arrays are supported" }

    val levels = 31 - length.countLeadingZeroBits()
    require(1 shl levels == length) { "Length must be a power of 2, got $length" }

    val cosLookup = DoubleArray(length / 2) {
        cos(2 * PI * it / length)
    }
    val sinLookup = DoubleArray(length / 2) {
        sin(2 * PI * it / length)
    }
    val out = this.copy()

    for (i in 0 until length) {
        val j = i.reverse() ushr 32 - levels
        if (j > i) {
            var temp = out[i, 0]
            out[i, 0] = out[j, 0]
            out[j, 0] = temp
            temp = out[i, 1]
            out[i, 1] = out[j, 1]
            out[j, 1] = temp
        }
    }

    var size = 2
    while (size <= length) {
        val halfSize = size / 2
        val step = length / size
        var i = 0
        while (i < length) {
            var j = i
            var k = 0
            while (j < i + halfSize) {
                val l = j + halfSize
                val tmpReal = out[l, 0] * cosLookup[k] + out[l, 1] * sinLookup[k]
                val tmpImag = -out[l, 0] * sinLookup[k] + out[l, 1] * cosLookup[k]
                out[l, 0] = out[j, 0] - tmpReal
                out[l, 1] = out[j, 1] - tmpImag
                out[j, 0] += tmpReal
                out[j, 1] += tmpImag
                j++
                k += step
            }
            i += size
        }
        if (size == length) {
            break
        }
        size *= 2
    }

    return out
}

/**
 * Performs an Inverse Fast Fourier Transform on this array.
 * The output is not normalized.
 *
 * @receiver The 1D Complex array to perform the IFFT on.
 * @return The 1D Complex IFFT of this array.
 */
fun F64Array.ifftComplexToComplex1D(): F64Array {
    require(shape.size == 2) { "Only 1D arrays are supported" }
    require(shape[1] == 2) { "Only complex arrays are supported" }

    val out = this.copy()

    // Conjugate
    for (i in 0 until length) {
        out[i, 1] = -out[i, 1]
    }

    out.fftComplexToComplex1D()

    // Conjugate
    for (i in 0 until length) {
        out[i, 1] = -out[i, 1] / length
    }

    return out
}

/**
 * Performs a 2D Fast Fourier Transform on this array.
 * The output is not normalized.
 *
 * @receiver The 2D real array to perform the FFT on.
 * @return The 2D Complex FFT of this array.
 */
fun F64Array.fftRealToComplex2D(): F64Array {
    require(shape.size == 2) { "Only 2D arrays are supported" }

    val out = F64Array(shape[0], shape[1], 2)
    copyTo(out.view(0, axis = 2))
    out.view(1, axis = 2).fill(0.0)

    return out.fftComplexToComplex2D()
}

/**
 * Performs a 2D Fast Fourier Transform on this array.
 * The output is not normalized.
 *
 * @receiver The 2D Complex array to perform the FFT on.
 * @return The 2D Complex FFT of this array.
 */
fun F64Array.fftComplexToComplex2D(): F64Array {
    require(shape.size == 3) { "Only 2D arrays are supported" }
    require(shape[2] == 2) { "Only complex arrays are supported" }

    var levels = 31 - shape[0].countLeadingZeroBits()
    require(1 shl levels == shape[0]) { "Dimension 1 must be a power of 2, got ${shape[0]}" }

    levels = 31 - shape[1].countLeadingZeroBits()
    require(1 shl levels == shape[1]) { "Dimension 2 must be a power of 2, got ${shape[0]}" }

    val out = this.copy()
    for (i in 0 until shape[0]) {
        val view = out.view(i, axis = 1)
        view.fftComplexToComplex1D().copyTo(view)
    }
    for (i in 0 until shape[1]) {
        val view = out.view(i, axis = 0)
        view.fftComplexToComplex1D().copyTo(view)
    }

    return out
}

/**
 * Performs an Inverse 2D Fast Fourier Transform on this array.
 * The output is not normalized.
 *
 * @receiver The 2D Complex array to perform the IFFT on.
 * @return The 2D Complex IFFT of this array.
 */
fun F64Array.ifftComplexToComplex2D(): F64Array {
    require(shape.size == 3) { "Only 2D arrays are supported" }
    require(shape[2] == 2) { "Only complex arrays are supported" }

    val out = this.copy()
    for (i in 0 until shape[0]) {
        val view = out.view(i, axis = 1)
        view.ifftComplexToComplex1D().copyTo(view)
    }
    for (i in 0 until shape[1]) {
        val view = out.view(i, axis = 0)
        view.ifftComplexToComplex1D().copyTo(view)
    }

    return out
}
