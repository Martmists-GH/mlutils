package com.martmists.mlutils.compat.dataframe

import com.martmists.ndarray.simd.F64Array
import com.martmists.ndarray.simd.F64FlatArray
import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.api.toDoubleArray

/**
 * Converts a [DataColumn] to an [F64FlatArray].
 */
fun DataColumn<Number>.toF64Array(): F64FlatArray {
    return F64Array.of(toDoubleArray())
}

/**
 * Converts an [F64FlatArray] to a [DataColumn].
 *
 * @param name The name of the column.
 */
fun F64FlatArray.toDataColumn(name: String): DataColumn<Double> {
    return DataColumn.create<Double>(name, this.toDoubleArray().toList())
}
