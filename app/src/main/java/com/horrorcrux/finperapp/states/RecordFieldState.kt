package com.horrorcrux.finperapp.states

import java.util.Date

data class TransactionTypeState(
    var transactionType: String = ""
)

data class TransactionDateState(
    var transactionDate: Date? = null
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