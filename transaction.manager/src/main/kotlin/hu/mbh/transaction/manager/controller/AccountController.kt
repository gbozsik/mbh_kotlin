package hu.mbh.transaction.manager.controller

import hu.mbh.transaction.manager.model.AccountResponse
import hu.mbh.transaction.manager.model.CreateAccountRequest
import hu.mbh.transaction.manager.model.SecurityCheckResultRequest
import hu.mbh.transaction.manager.service.AccountService
import jakarta.websocket.server.PathParam
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/account")
class AccountController(
        private val accountService: AccountService
) {

    @PostMapping
    fun createAccount(@RequestBody request: CreateAccountRequest): ResponseEntity<HttpStatus>? {
        accountService.createAccount(request)
        return ResponseEntity(HttpStatus.PRECONDITION_REQUIRED)
    }

    @GetMapping
    fun getAccounts(): ResponseEntity<List<AccountResponse>> {
        return ResponseEntity(accountService.getAllAccounts(), HttpStatus.OK)
    }

    @GetMapping("/{accountNumber}")
    fun getAccount(@PathParam("accountNumber") accountNumber: Long): ResponseEntity<AccountResponse> {
        return ResponseEntity(accountService.getAccount(accountNumber), HttpStatus.OK)
    }

    @PostMapping("/security-check")
    fun securityCheck(@RequestBody request: SecurityCheckResultRequest) {
        accountService.validateAccount(request)
    }
}