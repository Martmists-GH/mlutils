package com.martmists.mlutils.compat.langchain

import com.martmists.mlutils.compat.jvm.toFloatArray
import dev.langchain4j.data.embedding.Embedding
import com.martmists.ndarray.simd.F64Array
import com.martmists.ndarray.simd.F64FlatArray

/**
 * Converts an [Embedding] to an [F64FlatArray].
 */
fun Embedding.toF64Array(): F64FlatArray {
    val arr = this.vector().map { it.toDouble() }.toDoubleArray()
    return F64Array(arr.size) { arr[it] }.flatten()
}

/**
 * Converts an [F64FlatArray] to an [Embedding].
 */
fun F64FlatArray.toEmbedding(): Embedding {
    val arr = this.toFloatArray()
    return Embedding.from(arr)
}
