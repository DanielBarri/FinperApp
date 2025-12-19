package com.horrorcrux.finperapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.horrorcrux.finperapp.db.RecordsDao
import com.horrorcrux.finperapp.db.models.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecordsRepository(private val recordsDao: RecordsDao) {

    suspend fun insert(record: Record) {
        withContext(Dispatchers.IO) {
            try {
                recordsDao.insert(record)
            } catch (e: Exception) {
                Log.e("RecordsRepository", "Error inserting record", e)
                throw e
            }
        }
    }

    suspend fun update(record: Record) {
        withContext(Dispatchers.IO) {
            try {
                recordsDao.update(record)
            } catch (e: Exception) {
                Log.e("RecordsRepository", "Error updating record", e)
                throw e
            }
        }
    }

    fun all() : LiveData<List<Record>> {
        return  recordsDao.all()
    }

    suspend fun findById(id: Int) : Record {
        return withContext(Dispatchers.IO) {
            try {
                recordsDao.findById(id)
            } catch (e: Exception) {
                Log.e("RecordsRepository", "Error finding record by id", e)
                throw e
            }
        }
    }

    suspend fun delete(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                recordsDao.delete(id)
            } catch (e: Exception) {
                Log.e("RecordsRepository", "Error deleting record", e)
                throw e
            }
        }
    }
}

