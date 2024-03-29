package hu.mbh.transaction.manager.controller

import hu.mbh.transaction.manager.model.AccountResponse
import hu.mbh.transaction.manager.model.CreateAccountRequest
import hu.mbh.transaction.manager.model.SecurityCheckResultRequest
import hu.mbh.transaction.manager.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountService: AccountService
) {

    @PostMapping
    fun createAccount(@RequestBody request: CreateAccountRequest): ResponseEntity<HttpStatus>? {
        accountService.createAccount(request)
        return ResponseEntity(HttpStatus.PRECONDITION_REQUIRED)
    }

    @GetMapping
    suspend fun getAccounts(): ResponseEntity<List<AccountResponse>> {
        return ResponseEntity(accountService.getAllAccounts(), HttpStatus.OK)
    }

    @GetMapping("/{accountNumber}")
    fun getAccount(@PathVariable("accountNumber") accountNumber: Long): ResponseEntity<AccountResponse> {
        return ResponseEntity(accountService.getAccount(accountNumber), HttpStatus.OK)
    }

    @PostMapping("/security-check")
    fun securityCheck(@RequestBody request: SecurityCheckResultRequest) {
        accountService.validateAccount(request)
    }

    @DeleteMapping("/{accountNumber}")
    fun deleteAccount(@PathVariable("accountNumber") accountNumber: Long): ResponseEntity<HttpStatus> {
        accountService.deleteAccount(accountNumber)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}