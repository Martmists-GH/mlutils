package com.martmists.mlutils.compat.format

import com.martmists.mlutils.compat.jvm.ofRows
import org.jetbrains.bio.viktor.F64Array
import java.io.File

/**
 * Reads a CSV file into an [F64Array].
 *
 * @param csv The CSV file to read.
 * @param separator The separator between columns.
 * @return The [F64Array] read from the CSV file.
 */
fun F64Array.Companion.fromCSV(csv: File, separator: String = ","): F64Array {
    return csv.useLines { lines ->
        var length = 0
        val rows = mutableListOf<DoubleArray>()

        for ((i, line) in lines.withIndex()) {
            val parts = line.split(separator)
            if (parts.size != length) {
                if (length != 0) {
                    throw IllegalArgumentException("Inconsistent number of columns in CSV file at line $i")
                }
                length = parts.size
            }

            rows.add(parts.map { it.toDouble() }.toDoubleArray())
        }

        F64Array.Companion.ofRows(rows)
    }
}

/**
 * Writes an [F64Array] to a CSV file.
 *
 * @param csv The CSV file to write to.
 * @param separator The separator between columns.
 */
fun F64Array.toCSV(csv: File, separator: String = ",") {
    require(shape.size == 2) { "Only 2D arrays can be written to CSV" }

    csv.writer().use {
        for (i in 0 until shape[0]) {
            for (j in 0 until shape[1]) {
                it.append(get(i, j).toString())
                if (j < shape[1] - 1) {
                    it.append(separator)
                }
            }
            it.append("\n")
        }
    }
}
