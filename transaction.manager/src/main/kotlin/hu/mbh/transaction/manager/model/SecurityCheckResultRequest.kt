package hu.mbh.transaction.manager.model

data class SecurityCheckResultRequest(
        val accountNumber: Long,
        val isSecurityCheckSuccess: Boolean
)
