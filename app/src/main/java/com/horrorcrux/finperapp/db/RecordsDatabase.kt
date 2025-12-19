package com.horrorcrux.finperapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.horrorcrux.finperapp.db.models.Record

@Database(entities = [(Record::class)], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class RecordsDatabase : RoomDatabase() {

    abstract fun recordsDao() : RecordsDao

    companion object {
        @Volatile
        private var INSTANCE: RecordsDatabase? = null

        /**
         * Example migration for future schema changes.
         * When you need to change the database schema:
         * 1. Increment the version number in @Database annotation
         * 2. Create a new Migration object like the example below
         * 3. Add it to the .addMigrations() call
         *
         * Example:
         * val MIGRATION_1_2 = object : Migration(1, 2) {
         *     override fun migrate(database: SupportSQLiteDatabase) {
         *         // Add your SQL migration statements here
         *         database.execSQL("ALTER TABLE records ADD COLUMN new_column TEXT")
         *     }
         * }
         */

        fun getInstance(context: Context) : RecordsDatabase{
            synchronized(this){
                var instance = this.INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecordsDatabase::class.java,
                        "records_database"
                    )
                        // Add migrations here when schema changes
                        // .addMigrations(MIGRATION_1_2)
                        // For development: drops and recreates DB on schema changes
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