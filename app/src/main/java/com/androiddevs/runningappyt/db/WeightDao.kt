package com.supinfo.supfitness.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WeightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: WeightItems)

    @Delete
    fun delete(item: WeightItems)

    @Query("SELECT * FROM weight_items ORDER BY ItemName DESC")
    fun getAllWeightItems() : LiveData<List<WeightItems>>

    @Query("SELECT * FROM weight_items ORDER BY ItemQuantity ASC")
    fun getEntriesForGraph(): LiveData<List<WeightItems>>

}