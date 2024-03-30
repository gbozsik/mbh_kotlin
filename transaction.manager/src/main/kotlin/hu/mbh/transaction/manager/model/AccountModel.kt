package hu.mbh.transaction.manager.model

data class AccountModel(
        val accountNumber: Long,
        val accountHolderName: String,
        var balance: Long = 0,
        var validated: Boolean = false,
        var deleted: Boolean = false
)
