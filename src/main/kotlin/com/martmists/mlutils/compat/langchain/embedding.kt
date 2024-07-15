package com.martmists.mlutils.compat.langchain

import dev.langchain4j.data.embedding.Embedding
import org.jetbrains.bio.viktor.F64Array
import org.jetbrains.bio.viktor.F64FlatArray

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
    val arr = this.toArray().map { it.toFloat() }.toFloatArray()
    return Embedding.from(arr)
}
