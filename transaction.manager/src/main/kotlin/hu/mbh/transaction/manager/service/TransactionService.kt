package hu.mbh.transaction.manager.service

import hu.mbh.transaction.manager.dao.AccountDao
import hu.mbh.transaction.manager.dao.TransactionDao
import hu.mbh.transaction.manager.exception.BusinessException
import hu.mbh.transaction.manager.model.AccountModel
import hu.mbh.transaction.manager.model.CreateTransactionRequest
import hu.mbh.transaction.manager.model.ErrorCode
import hu.mbh.transaction.manager.model.TransactionResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionService(val accountDao: AccountDao, val transactionDao: TransactionDao) {

    companion object {
        private const val TRANSACTION_FAILED_MESSAGE = "Failed to process transaction"
    }

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun createTransaction(request: CreateTransactionRequest) {
        validateRequest(request)
        val accountModel = accountDao.getAccount(request.accountNumber)
        accountModel?.also {
            validateAccount(it)
            it.balance += request.amount
            accountDao.updateAccount(it)
            transactionDao.saveTransaction(request)
        } ?: run {
            logger.error("Account not found for transaction: {}", request.accountNumber)
            throw BusinessException(TRANSACTION_FAILED_MESSAGE, ErrorCode.TRANSACTION_005)
        }
    }

    private fun validateAccount(accountModel: AccountModel) {
        if (!accountModel.validated) {
            logger.error("Validation error. Account not validated with account number: {}", accountModel.accountNumber)
            throw BusinessException(TRANSACTION_FAILED_MESSAGE, ErrorCode.TRANSACTION_008)
        }
    }

    private fun validateRequest(request: CreateTransactionRequest) {
        if (request.timestamp < LocalDateTime.now()) {
            logger.error("Validation error: ${ErrorCode.TRANSACTION_002.description}")
            throw BusinessException(TRANSACTION_FAILED_MESSAGE, ErrorCode.TRANSACTION_002)
        }
    }
    fun getAllTransactions(): List<TransactionResponse> {
        return transactionDao.getAllTransactions().stream()
                .map { TransactionResponse(it.accountNumber, it.amount, it.type, it.timestamp) }
                .toList()
    }
}