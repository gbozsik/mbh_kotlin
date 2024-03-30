package hu.mbh.transaction.manager.service

import hu.mbh.transaction.manager.client.SecurityCheckClient
import hu.mbh.transaction.manager.dao.AccountDao
import hu.mbh.transaction.manager.exception.BusinessException
import hu.mbh.transaction.manager.model.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class AccountService(
    val accountDao: AccountDao,
    val securityCheckClient: SecurityCheckClient,
    @Value("\${client.url.security-check.callback}") val securityCheckCallbackUrl: String
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun createAccount(request: CreateAccountRequest) {
        logger.info("Creating account with account number: {}", request.accountNumber)
        accountDao.createAccount(request)
        callSecurityCheckServiceAsync(request)
    }

    private fun callSecurityCheckServiceAsync(request: CreateAccountRequest) {
        GlobalScope.launch {
            securityCheckClient.callSecurityCheckClient(
                SecurityCheckRequest(request.accountNumber, request.accountHolderName, securityCheckCallbackUrl)
            )
        }
    }

    fun validateAccount(request: SecurityCheckResultRequest) {
        accountDao.getAccount(request.accountNumber)?.also {
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
            logger.error("Account could not be validated because no account found with account number: {}", request.accountNumber)
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
            AccountResponse(it.accountNumber, it.accountHolderName, it.balance, it.validated)
        } ?: let {
            logger.error(ErrorCode.TRANSACTION_004.description)
            throw BusinessException("Error occurred during get account", ErrorCode.TRANSACTION_004, HttpStatus.NOT_FOUND)
        }
    }

    fun deleteAccount(accountNumber: Long) {
        accountDao.getAccount(accountNumber)?.also {
            it.deleted = true
            accountDao.updateAccount(it)
        } ?: run {
            logger.error("Account could not be deleted because no account found with account number: {}", accountNumber)
            throw BusinessException("Error during validating account", ErrorCode.TRANSACTION_004)
        }
    }
}