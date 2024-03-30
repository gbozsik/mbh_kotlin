package hu.mbh.transaction.manager.model

import java.time.LocalDateTime

data class CreateTransactionRequest(
        val accountNumber: Long,
        val amount: Long,
        val type: String,
        val timestamp: LocalDateTime
) {
}