package com.martmists.mlutils.compat.exposed

import com.martmists.mlutils.compat.jvm.of
import com.martmists.mlutils.compat.jvm.toFloatArray
import com.pgvector.PGvector
import org.jetbrains.bio.viktor.F64Array
import org.jetbrains.bio.viktor.F64FlatArray
import org.jetbrains.exposed.sql.ColumnType
import java.sql.ResultSet

/**
 * [ColumnType] for [F64FlatArray] using [PGvector].
 */
internal class PgVectorColumnType(private val size: Int) : ColumnType<F64FlatArray>() {
    override fun sqlType(): String = "vector($size)"

    override fun readObject(rs: ResultSet, index: Int) = rs.getObject(index) as PGvector?

    override fun validateValueBeforeUpdate(value: F64FlatArray?) {
        when (value) {
            is F64FlatArray -> require(value.shape.contentEquals(intArrayOf(size))) { "F64FlatArray dimension must be $size" }
            else -> error("Unexpected value: $value")
        }
    }

    override fun valueFromDB(value: Any) = when (value) {
        is PGvector -> F64Array.of(value.toArray())
        else -> error("Unexpected value: $value")
    }

    override fun notNullValueToDB(value: F64FlatArray) = PGvector(value.flatten().toFloatArray())
}
