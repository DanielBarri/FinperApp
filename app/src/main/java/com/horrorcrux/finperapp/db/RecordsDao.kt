package com.horrorcrux.finperapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.horrorcrux.finperapp.db.models.Record

@Dao
interface RecordsDao {
    @Insert
    suspend fun insert(record: Record)

    @Update
    suspend fun update(record: Record)

    @Query("DELETE FROM records WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM records")
    fun all() : LiveData<List<Record>>

    @Query("SELECT * FROM records WHERE id = :id")
    suspend fun findById(id: Int) : Record
}

