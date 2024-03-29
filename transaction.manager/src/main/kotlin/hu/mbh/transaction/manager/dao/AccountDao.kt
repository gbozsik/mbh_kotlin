package hu.mbh.transaction.manager.dao

import hu.mbh.transaction.manager.exception.BusinessException
import hu.mbh.transaction.manager.model.AccountModel
import hu.mbh.transaction.manager.model.CreateAccountRequest
import hu.mbh.transaction.manager.model.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class AccountDao {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val accountStore: ConcurrentHashMap<Long, AccountModel> = ConcurrentHashMap()

    fun createAccount(request: CreateAccountRequest) {
        val result = accountStore.putIfAbsent(request.accountNumber,
                AccountModel(accountNumber = request.accountNumber, accountHolderName = request.accountHolderName, balance = 0))
        if (result != null) {
            logger.error("Account already exists with account number: {}", request.accountNumber)
            throw BusinessException("Failed to create account", ErrorCode.TRANSACTION_001)
        }
    }

    fun getAccount(accountNumber: Long): AccountModel? {
        return accountStore[accountNumber]?.let {
            return if (!it.deleted) it
            else null
        }
    }

    fun updateAccount(accountModel: AccountModel) {
        accountStore[accountModel.accountNumber] = accountModel
    }

    fun getAllAccount(): MutableCollection<AccountModel> {
        return accountStore.values
    }
}