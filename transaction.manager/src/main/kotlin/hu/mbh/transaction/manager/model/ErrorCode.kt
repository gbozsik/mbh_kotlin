package hu.mbh.transaction.manager.model

enum class ErrorCode(val description: String) {
    TRANSACTION_001("Account already exists"),
    TRANSACTION_002("Transaction timestamp cannot represent past"),
    TRANSACTION_003("Transaction already exists"),
    TRANSACTION_004("Account not found"),
    TRANSACTION_005("Transaction creation failed"),
    TRANSACTION_006("Could not parse TransactionType"),
    TRANSACTION_007("Unexpected error"),
    TRANSACTION_008("Account not validated")
}