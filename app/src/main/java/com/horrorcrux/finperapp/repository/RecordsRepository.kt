package com.horrorcrux.finperapp.repository

import androidx.lifecycle.LiveData
import com.horrorcrux.finperapp.db.RecordsDao
import com.horrorcrux.finperapp.db.models.Record
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordsRepository(private val recordsDao: RecordsDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insert(record: Record) {
        coroutineScope.launch(Dispatchers.IO){
            recordsDao.insert(record)
        }
    }

    fun update(record: Record) {
        coroutineScope.launch(Dispatchers.IO){
            recordsDao.update(record)
        }
    }

    fun all() : LiveData<List<Record>> {
        return  recordsDao.all()
    }

    suspend fun findById(id: Int) : Record {
        return recordsDao.findById(id)
    }

    fun delete(id: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            recordsDao.delete(id)
        }
    }
}

