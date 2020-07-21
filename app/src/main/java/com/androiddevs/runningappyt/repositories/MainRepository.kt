package com.androiddevs.runningappyt.repositories

import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDAO
) {
    suspend fun insertRun(run: Run) = runDao.insertRun(run)
    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()
    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistance()
    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()
    fun getAllRunsSortedByCaloriesBurnt() = runDao.getAllRunsSortedByCaloriesBurnt()
    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByAvgSpeed()

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()
    fun getTotalDistance() = runDao.getTotalDistance()
    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()
    fun getTotalCaloriesBurnt() = runDao.getTotalCaloriesBurnt()
}
