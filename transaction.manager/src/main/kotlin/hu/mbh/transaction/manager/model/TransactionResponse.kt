package hu.mbh.transaction.manager.model

import java.time.LocalDateTime

data class TransactionResponse(
        val accountNumber: Long,
        val amount: Long,
        val type: TransactionType,
        val timestamp: LocalDateTime
)
