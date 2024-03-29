package hu.mbh.transaction.manager.model

data class CreateAccountRequest(
        val accountNumber: Long,
        val accountHolderName: String,
)
