package com.martmists.mlutils.compat.exposed

import com.pgvector.PGvector
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

/**
 *  [PGVectorManager] is an implementation of [TransactionManager] that adds the [PGvector] type to the current transaction. This is required for the extension to work.
 *
 *  Due to its simplicity, this class is licensed under [CC0](https://en.wikipedia.org/wiki/Creative_Commons_license#Zero,_public_domain).
 */
internal class PGVectorManager(private val impl: TransactionManager) : TransactionManager by impl {
    override fun newTransaction(isolation: Int, readOnly: Boolean, outerTransaction: Transaction?): Transaction {
        val transaction = impl.newTransaction(isolation, readOnly, outerTransaction)
        val conn = transaction.connection.connection as Connection
        PGvector.addVectorType(conn)
        return transaction
    }
}
