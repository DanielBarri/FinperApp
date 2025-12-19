package com.horrorcrux.finperapp.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "records")
data class Record(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "transactionType")
    val transactionType: TransactionType,

    @ColumnInfo(name = "date")
    val transactionDate: Date?,

    @ColumnInfo(name= "category")
    val category: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "amount")
    val amount: Double,
)