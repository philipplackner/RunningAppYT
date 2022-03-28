package com.androiddevs.runningappyt.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Run::class], version = 1)
@TypeConverters(Converters::class)
abstract class RunningDB : RoomDatabase() {
    abstract fun getRunDao(): RunDao

}