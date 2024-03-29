package hu.mbh.transaction.manager.service

import hu.mbh.transaction.manager.client.SecurityCheckClient
import hu.mbh.transaction.manager.dao.AccountDao
import hu.mbh.transaction.manager.exception.BusinessException
import hu.mbh.transaction.manager.model.*
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AccountService(
        val accountDao: AccountDao,
        val securityCheckClient: SecurityCheckClient
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun createAccount(request: CreateAccountRequest) {
        logger.info("Creating account with account number: {}", request.accountNumber)
        accountDao.createAccount(request)
        GlobalScope.launch {
            securityCheckClient.callSecurityCheckClient(
                    SecurityCheckRequest(request.accountNumber, request.accountHolderName, "http://localhost:8080/account/security-check"))//todo urls from property
        }
    }

    fun validateAccount(request: SecurityCheckResultRequest) {
        val accountModel = accountDao.getAccount(request.accountNumber)
        accountModel?.also {
            if (request.isSecurityCheckSuccess) {
                it.validated = true
                logger.info("Validation successful for account: {}", request.accountNumber)
            } else {
                it.deleted = true
                it.validated = false
                logger.info("Validation failed for account: {}", request.accountNumber)
                logger.info("Account is set to deleted")
            }
            accountDao.updateAccount(it)
        } ?: run {
            logger.error("Account not found with account number: {}", request.accountNumber)
            throw BusinessException("Error during validating account", ErrorCode.TRANSACTION_004)
        }
    }

    fun getAllAccounts(): List<AccountResponse> {
        return accountDao.getAllAccount().stream()
                .filter { !it.deleted }
                .map { AccountResponse(it.accountNumber, it.accountHolderName, it.balance, it.validated) }
                .toList()
    }

    fun getAccount(accountNumber: Long): AccountResponse? {
        return accountDao.getAccount(accountNumber)?.let {
            return if (!it.deleted) AccountResponse(it.accountNumber, it.accountHolderName, it.balance, it.validated)
            else null
        } ?: let {
            logger.error(ErrorCode.TRANSACTION_004.description)
            throw BusinessException("Error occurred during get account", ErrorCode.TRANSACTION_004)
        }
    }
}