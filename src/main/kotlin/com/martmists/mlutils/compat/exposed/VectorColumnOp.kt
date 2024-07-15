package com.martmists.mlutils.compat.exposed

import org.jetbrains.bio.viktor.F64FlatArray
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.append

/**
 * Represents a Vector comparison between `left` and `right`, where `left` is a [Column] and `right` is a [Column].
 */
internal class VectorColumnOp(
    private val left: Column<F64FlatArray>,
    private val right: Column<F64FlatArray>,
    private val op: String
) : Op<Float>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) = queryBuilder {
        append(left, " $op ", right)
    }
}
