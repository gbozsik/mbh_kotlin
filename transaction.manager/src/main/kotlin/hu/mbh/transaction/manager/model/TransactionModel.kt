package hu.mbh.transaction.manager.model

import java.time.LocalDateTime
import java.util.*

data class TransactionModel(
        val id: String = UUID.randomUUID().toString(),
        val accountNumber: Long,
        val amount: Long,
        val type: TransactionType,
        val timestamp: LocalDateTime
)
