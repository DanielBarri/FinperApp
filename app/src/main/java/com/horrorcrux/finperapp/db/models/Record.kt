package com.horrorcrux.finperapp.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "records")
data class Record(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "transactionType")
    var transactionType: String,

    @ColumnInfo(name = "date")
    var transactionDate: Date,

    @ColumnInfo(name= "category")
    var category: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "amount")
    var amount: Double,
)