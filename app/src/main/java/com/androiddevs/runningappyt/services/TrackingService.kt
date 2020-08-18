package com.androiddevs.runningappyt.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.other.Constants.ACTION_PAUSE_SERVICE
import com.androiddevs.runningappyt.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.androiddevs.runningappyt.other.Constants.ACTION_START_RESUME_SERVICE
import com.androiddevs.runningappyt.other.Constants.ACTION_STOP_SERVICE
import com.androiddevs.runningappyt.other.Constants.FASTEST_LOCATION_INTERVAL
import com.androiddevs.runningappyt.other.Constants.LOCATION_UPDATE_INTERVAL
import com.androiddevs.runningappyt.other.Constants.NOTIFICATION_CHANNEL_ID
import com.androiddevs.runningappyt.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.androiddevs.runningappyt.other.Constants.NOTIFICATION_ID
import com.androiddevs.runningappyt.other.Constants.TIMER_UPDATE_INTERVAL
import com.androiddevs.runningappyt.other.TrackingUtility
import com.androiddevs.runningappyt.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

class TrackingService : LifecycleService() {

    private var isFirstRun = true
    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimestamp = 0L
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val timeRunInSeconds = MutableLiveData<Long>()

    companion object {
        val timeRunInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<PolyLines>()
    }

    override fun onCreate() {
        super.onCreate()
        init()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    private fun init() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_RESUME_SERVICE ->
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        startTimer()
                    }
                ACTION_PAUSE_SERVICE -> pauseService()

                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result?.locations?.let { locations ->
                    locations.forEach {
                        addPathPoint(it)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if(isTracking) {
            if (TrackingUtility.askLocationPermissions(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
            }
        } else fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted

                timeRunInMillis.postValue(timeRun + lapTime)

                if (timeRunInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(it.latitude, it.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(false)
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                .setContentTitle("Running App")
                .setContentText("00:00:00")
                .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel =
            NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }
}