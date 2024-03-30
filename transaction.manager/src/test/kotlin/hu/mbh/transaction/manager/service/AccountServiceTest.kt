package hu.mbh.transaction.manager.service

import hu.mbh.transaction.manager.client.SecurityCheckClient
import hu.mbh.transaction.manager.dao.AccountDao
import hu.mbh.transaction.manager.exception.BusinessException
import hu.mbh.transaction.manager.model.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AccountServiceTest {

    private val accountNumber: Long = 1234567
    private val accountHolderName = "account holder name"
    private val balance: Long = 9999

    private val accountDao: AccountDao = mockk<AccountDao> {
        every { getAccount(any()) } returns AccountModel(accountNumber, accountHolderName, balance, false, false)
    }
    private val securityCheckClient: SecurityCheckClient = mockk<SecurityCheckClient>()
    private val accountService: AccountService = AccountService(accountDao, securityCheckClient, "securityCheckUrl")

    @Test
    fun createAccountTest() {
        val createAccountRequestSlot = slot<CreateAccountRequest>()
        val request = CreateAccountRequest(accountNumber, accountHolderName)
        every { accountDao.createAccount(capture(createAccountRequestSlot)) } answers {}

        accountService.createAccount(request)

        assertEquals(accountNumber, createAccountRequestSlot.captured.accountNumber)
        verify { securityCheckClient.callSecurityCheckClient(any(SecurityCheckRequest::class)) }
    }

    @Test
    fun validateAccountTest() {
        val request = SecurityCheckResultRequest(accountNumber, true)
        val accountModelSlot = slot<AccountModel>()
        every { accountDao.updateAccount(capture(accountModelSlot)) } answers {}

        accountService.validateAccount(request)

        assertTrue(accountModelSlot.captured.validated)
    }

    @Test
    fun validateAccountTest_ValidationFalse() {
        val request = SecurityCheckResultRequest(accountNumber, false)
        val accountModelSlot = slot<AccountModel>()
        every { accountDao.updateAccount(capture(accountModelSlot)) } answers {}

        accountService.validateAccount(request)

        assertFalse(accountModelSlot.captured.validated)
        assertTrue(accountModelSlot.captured.deleted)
    }

    @Test
    fun validateAccountTest_AccountNotFound() {
        val request = SecurityCheckResultRequest(accountNumber, false)
        every { accountDao.getAccount(request.accountNumber) } returns null

        val exception = assertThrows<BusinessException> { accountService.validateAccount(request) }

        assertEquals(ErrorCode.TRANSACTION_004, exception.errorCode)
        verify(exactly = 0) { accountDao.updateAccount(any()) }
    }

    @Test
    fun getAllAccountsTest() {
        //GIVEN
        val accountModel1 = AccountModel(accountNumber, accountHolderName)
        val holderOfDeletedAccount = "holder-of-deleted"
        val accountModelDeleted = AccountModel(accountNumber, holderOfDeletedAccount, deleted = true)
        val accountModel2 = AccountModel(accountNumber, accountHolderName, validated = true)
        val accounts = listOf(accountModel1, accountModelDeleted, accountModel2)
        every { accountDao.getAllAccount() } returns accounts

        //WHEN
        val result = accountService.getAllAccounts()

        //THEN
        assertEquals(2, result.size)
        val foundDeletedAccount: Boolean = result.stream()
            .anyMatch { account -> account.accountHolderName == holderOfDeletedAccount }
        assertFalse(foundDeletedAccount)

    }

    @Test
    fun getAccountTest() {
        val result = accountService.getAccount(accountNumber)

        assertEquals(accountNumber, result!!.accountNumber)
    }

    @Test
    fun getAccountTest_NotFound() {
        every { accountDao.getAccount(accountNumber) } returns null

        val exception = assertThrows<BusinessException> { accountService.getAccount(accountNumber) }

        assertEquals(ErrorCode.TRANSACTION_004, exception.errorCode)
    }

    @Test
    fun deleteAccountTest() {
        val accountModelSlot = slot<AccountModel>()
        every { accountDao.updateAccount(capture(accountModelSlot)) } answers {}

        accountService.deleteAccount(accountNumber)

        assertTrue(accountModelSlot.captured.deleted)
    }

    @Test
    fun deleteAccountTest_AccountNotFound() {
        every { accountDao.getAccount(accountNumber) } returns null

        val exception = assertThrows<BusinessException> { accountService.deleteAccount(accountNumber) }

        assertEquals(ErrorCode.TRANSACTION_004, exception.errorCode)
        verify(exactly = 0) { accountDao.updateAccount(any()) }
    }
}