package com.supinfo.supfitness.repositories

import com.supinfo.supfitness.database.WeightDataBase
import com.supinfo.supfitness.database.WeightItems

class WeightRepository(private val db: WeightDataBase) {

    suspend fun insert(items: WeightItems) = db.getWeightDao().insert(items)
    suspend fun delete(items: WeightItems) = db.getWeightDao().delete(items)

    fun getAllItems() = db.getWeightDao().getAllWeightItems()



}