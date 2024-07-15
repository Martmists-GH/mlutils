package com.martmists.mlutils.compat.exposed

import kotlinx.coroutines.selects.select
import org.jetbrains.bio.viktor.F64FlatArray
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * A column type for storing vectors in PostgreSQL.
 *
 * @param name The name of the column.
 * @param size The size of the vector.
 */
fun Table.vector(name: String, size: Int = 384): Column<F64FlatArray> = registerColumn(name, PgVectorColumnType(size))

/**
 * Calculates the cosine distance between this and some [other] column.
 */
infix fun Column<F64FlatArray>.cosineDistance(other: Column<F64FlatArray>): Op<Float> {
    return VectorColumnOp(this, other, "<=>")
}

/**
 * Calculates the cosine distance between this and some [other] vector.
 */
infix fun Column<F64FlatArray>.cosineDistance(other: F64FlatArray): Op<Float> {
    return VectorValueOp(this, other, "<=>")
}

/**
 * Calculates the L2 distance between this and some [other] column.
 */
infix fun Column<F64FlatArray>.l2Distance(other: Column<F64FlatArray>): Op<Float> {
    return VectorColumnOp(this, other, "<->")
}

/**
 * Calculates the L2 distance between this and some [other] vector.
 */
infix fun Column<F64FlatArray>.l2Distance(other: F64FlatArray): Op<Float> {
    return VectorValueOp(this, other, "<->")
}

/**
 * Calculates the inner product between this and some [other] column.
 */
infix fun Column<F64FlatArray>.innerProduct(other: Column<F64FlatArray>): Op<Float> {
    return VectorColumnOp(this, other, "<#>")
}

/**
 * Calculates the inner product between this and some [other] vector.
 */
infix fun Column<F64FlatArray>.innerProduct(other: F64FlatArray): Op<Float> {
    return VectorValueOp(this, other, "<#>")
}

/**
 * Calculates the L1 distance between this and some [other] column.
 */
infix fun Column<F64FlatArray>.l1Distance(other: Column<F64FlatArray>): Op<Float> {
    return VectorColumnOp(this, other, "<+>")
}

/**
 * Calculates the L1 distance between this and some [other] vector.
 */
infix fun Column<F64FlatArray>.l1Distance(other: F64FlatArray): Op<Float> {
    return VectorValueOp(this, other, "<+>")
}

/**
 * Initializes the [Database] to use the pgvector extension and registers managers for the correct type.
 */
fun Database.usePGVector() {
    TransactionManager.registerManager(this, PGVectorManager(TransactionManager.manager))
    transaction(this) {
        exec("CREATE EXTENSION IF NOT EXISTS vector;")
    }
}
