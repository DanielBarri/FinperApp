package com.horrorcrux.finperapp.viewmodels

import com.horrorcrux.finperapp.db.models.TransactionType
import java.util.Date

sealed class Event {
    data class SetTransactionType(
        val transactionType: TransactionType,
    ) : Event()

    data class SetTransactionDate(
        val transactionDate: Date?,
    ) : Event()
    data class SetCategory(
        val category: String
    ) : Event()

    data class SetDescription(
        val description: String
    ) : Event()

    data class SetAmount(
        val amount: Double
    ) : Event()

    object OpenRecord: Event()

    object CloseRecord: Event()

    object Save: Event()

    data class ShowDeleteConfirmation(val id: Int?) : Event()

    object CancelDelete : Event()

    object ConfirmDelete : Event()

    data class Load(val id: Int?) : Event()

    data class ShowError(val message: String) : Event()
}