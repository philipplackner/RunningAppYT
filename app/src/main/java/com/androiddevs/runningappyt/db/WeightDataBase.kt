package com.supinfo.supfitness.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WeightItems::class], version = 3)
abstract class WeightDataBase : RoomDatabase() {

    abstract fun getWeightDao(): WeightDao

    companion object {
        @Volatile
        private var instance : WeightDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context?) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context?) =
            Room.databaseBuilder(
                context!!?.applicationContext,
                WeightDataBase::class.java,
                "Weight.db"
            ).fallbackToDestructiveMigration().build()
    }
}