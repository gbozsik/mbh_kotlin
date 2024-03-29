package hu.mbh.transaction.manager.controller

import hu.mbh.transaction.manager.model.CreateTransactionRequest
import hu.mbh.transaction.manager.model.TransactionResponse
import hu.mbh.transaction.manager.service.TransactionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transaction")
class TransactionController(val transactionService: TransactionService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    fun createTransaction(@RequestBody request: CreateTransactionRequest): ResponseEntity<HttpStatus> {
        logger.info("Processing transaction for account: {}", request.accountNumber)
        transactionService.createTransaction(request)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping
    fun getTransactions(): List<TransactionResponse> {
        return transactionService.getAllTransactions()
    }
}