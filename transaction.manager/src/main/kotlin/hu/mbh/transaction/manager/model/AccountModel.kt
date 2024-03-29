package hu.mbh.transaction.manager.model

data class AccountModel(
        val accountNumber: Long,
        val accountHolderName: String,
        var balance: Long,
        var validated: Boolean = false,
        var deleted: Boolean = false
)
