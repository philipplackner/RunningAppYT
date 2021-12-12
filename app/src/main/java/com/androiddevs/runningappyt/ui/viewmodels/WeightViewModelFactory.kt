package com.supinfo.supfitness.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.supinfo.supfitness.repositories.WeightRepository

class WeightViewModelFactory(private val repository: WeightRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return WeightViewModel(repository) as T
    }
}