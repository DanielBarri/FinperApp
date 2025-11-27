package com.horrorcrux.finperapp.states

data class TransactionTypeState(
    var transactionType: String = ""
)

data class CategoryState(
    var category: String = ""
)

data class DescriptionState(
    var description: String = ""
)

data class AmountState(
    var amount: Double = 0.0
)