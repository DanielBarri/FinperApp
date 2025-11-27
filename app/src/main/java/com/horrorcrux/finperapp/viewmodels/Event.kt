package com.horrorcrux.finperapp.viewmodels

import com.horrorcrux.finperapp.db.models.Record

sealed class Event {
    data class SetRecord (
        val transactionType: String,
        val category: String,
        val description: String,
        val amount: Double)
        : Event()

    object OpenRecord: Event()

    object CloseRecord: Event()

    object Save: Event()

    data class Delete(val id: Int?) : Event()

    data class Load(val id: Int?) : Event()
}