package com.androiddevs.runningappyt.other

import android.Manifest
import android.content.Context
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions
import java.sql.Time
import java.util.concurrent.TimeUnit

object TrackingUtility {

    fun askLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun getFormattedStopwatchTime(ms: Long, includeMillis: Boolean = false): String {
        var _ms = ms
        val hours = TimeUnit.MILLISECONDS.toHours(_ms)
        _ms -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(_ms)
        _ms -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(_ms)
        if (!includeMillis) {
            return "${if (hours < 10) "0" else ""}$hours:" +
                    "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if(seconds < 10) "0" else ""}$seconds"
        }
        _ms -= TimeUnit.SECONDS.toMillis(seconds)
        _ms /= 10
        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if(seconds < 10) "0" else ""}$seconds:" +
                "${if(_ms < 10) "0" else ""}$_ms"
    }
}