package com.horrorcrux.finperapp.states

import com.horrorcrux.finperapp.db.models.TransactionType
import java.util.Date

data class TransactionTypeState(
    val transactionType: TransactionType = TransactionType.INCOME
)

data class TransactionDateState(
    val transactionDate: Date? = null
)
data class CategoryState(
    val category: String = ""
)

data class DescriptionState(
    val description: String = ""
)

data class AmountState(
    val amount: Double = 0.0
)