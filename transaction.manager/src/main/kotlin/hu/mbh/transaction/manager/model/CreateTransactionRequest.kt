package hu.mbh.transaction.manager.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CreateTransactionRequest(
        val accountNumber: Long,
        val amount: Long,
        val type: String,
        @JsonProperty(required = false)
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        val timestamp: LocalDateTime
) {
}