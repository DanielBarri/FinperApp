package com.horrorcrux.finperapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.horrorcrux.finperapp.db.models.Record

@Database(entities = [(Record::class)], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecordsDatabase : RoomDatabase() {

    abstract fun recordsDao() : RecordsDao

    companion object {
        @Volatile
        private var INSTANCE: RecordsDatabase? = null

        fun getInstance(context: Context) : RecordsDatabase{
            synchronized(this){
                var instance = this.INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecordsDatabase::class.java,
                        "records_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    this.INSTANCE = instance
                    return instance

                } else {
                    return instance
                }
            }
        }
    }
}