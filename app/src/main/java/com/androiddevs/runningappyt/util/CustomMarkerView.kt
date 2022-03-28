package com.androiddevs.runningappyt.util

import android.content.Context
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    val runs: List<Run>,
    c: Context,
    layoutId: Int
) : MarkerView(c, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) {
            return
        }
        val curRunId = e.x.toInt()
        val run = runs[curRunId]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        findViewById<MaterialTextView>(R.id.tvDate).text = dateFormat.format(calendar.time)

        val avgSpeed = "${run.avgSpeedInKMH}km/h"
        findViewById<MaterialTextView>(R.id.tvAvgSpeed).text = avgSpeed

        val distanceInKm = "${run.distanceInMeter / 1000f}km"

        findViewById<MaterialTextView>(R.id.tvDistance).text = distanceInKm
        findViewById<MaterialTextView>(R.id.tvDuration).text =
            TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

        val caloriesBurned = "${run.caloriesBurned}kcal"
        findViewById<MaterialTextView>(R.id.tvCaloriesBurned).text = caloriesBurned
    }
}