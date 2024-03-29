package hu.mbh.transaction.manager.model

data class SecurityCheckRequest(
        val accountNumber: Long,
        val accountHolderName: String,
        val callbackUrl: String
)
