package com.horrorcrux.finperapp.states

data class RecordFieldState(
    var transactionType: String = "",
    var category: String = "",
    var description: String = "",
    var amount: Double = 0.0
)

