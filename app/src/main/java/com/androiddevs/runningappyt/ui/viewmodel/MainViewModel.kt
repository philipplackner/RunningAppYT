package com.androiddevs.runningappyt.ui.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.repositories.MainRepository
import com.androiddevs.runningappyt.util.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    private val runsSortedByDate = mainRepository.getAllRunsSortedByDate()
    private val runsSortedByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runsSortedByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = mainRepository.getAllRunsSortedByTimeInMillis()
    private val runsSortedByAvgSpeed = mainRepository.getAllRunsSortedByAvgSpeed()

    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE

    init {
        runs.addSource(runsSortedByDate) { res ->
            if (sortType == SortType.DATE) {
                res?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDistance) { res ->
            if (sortType == SortType.DISTANCE) {
                res?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCaloriesBurned) { res ->
            if (sortType == SortType.CALORIES_BURNED) {
                res?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMillis) { res ->
            if (sortType == SortType.RUNNING_TIME) {
                res?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByAvgSpeed) { res ->
            if (sortType == SortType.AVG_SPEED) {
                res?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) = when (sortType) {
        SortType.DATE -> runsSortedByDate.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
        SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        SortType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        SortType.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }

    fun insertRun(run: Run) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.insertRun(run)
    }
}