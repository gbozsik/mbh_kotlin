package hu.mbh.transaction.manager.model

data class AccountResponse(
        val accountNumber: Long,
        val accountHolderName: String,
        val balance: Long,
        val validated: Boolean
) {
}