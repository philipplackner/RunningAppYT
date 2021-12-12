package com.supinfo.supfitness.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.supinfo.supfitness.database.WeightItems
import com.supinfo.supfitness.repositories.WeightRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeightViewModel(private val repository: WeightRepository) : ViewModel() {

    fun insert(items: WeightItems) = GlobalScope.launch {
        repository.insert(items)
    }

    fun delete(items: WeightItems) = GlobalScope.launch {
        repository.delete(items)
    }

    fun getAllWeightItems() = repository.getAllItems()

}