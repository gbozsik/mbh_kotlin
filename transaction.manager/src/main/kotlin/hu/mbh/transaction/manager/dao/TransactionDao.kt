package hu.mbh.transaction.manager.dao

import hu.mbh.transaction.manager.exception.BusinessException
import hu.mbh.transaction.manager.model.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Component
class TransactionDao {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val transactionStore: ConcurrentHashMap<String, TransactionModel> = ConcurrentHashMap()

    fun saveTransaction(request: CreateTransactionRequest) {
        val transaction = TransactionModel(accountNumber = request.accountNumber, type = TransactionType.getByValue(request.type), amount = request.amount, timestamp = LocalDateTime.now())
        val result = transactionStore.putIfAbsent(transaction.id, transaction)
        if (result != null) {
            logger.error("Transaction already exists with id: {}", transaction.id)
            throw BusinessException("Failed to create transaction", ErrorCode.TRANSACTION_003)
        }
    }

    fun getAllTransactions(): MutableCollection<TransactionModel> {
        return transactionStore.values
    }
}