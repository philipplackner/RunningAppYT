package com.supinfo.supfitness.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_items")
data class WeightItems(

    @ColumnInfo(name = "itemName")
    var itemName: String,

    @ColumnInfo(name = "itemQuantity")
    var itemQuantity: Int,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

}