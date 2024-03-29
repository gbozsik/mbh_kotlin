package hu.mbh.transaction.manager.model

import hu.mbh.transaction.manager.exception.BusinessException
import org.springframework.util.Assert

enum class TransactionType(val value: String) {
    DEPOSIT("deposit"), WITHDRAWAL("withdrawal");

    companion object {
        fun getByValue(value: String): TransactionType {
            Assert.notNull(value, "TransactionType value cannot be null")
            for (transactionType in entries) {
                if (transactionType.value == value) return transactionType
            }
            throw BusinessException("TransactionType not found: $value", ErrorCode.TRANSACTION_006)
        }
    }
}