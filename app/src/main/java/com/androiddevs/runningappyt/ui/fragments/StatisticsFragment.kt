package com.androiddevs.runningappyt.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.databinding.FragmentStatisticsBinding
import com.androiddevs.runningappyt.ui.viewmodel.StatisticsViewModel
import com.androiddevs.runningappyt.util.CustomMarkerView
import com.androiddevs.runningappyt.util.TrackingUtility
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private val viewModel: StatisticsViewModel by viewModels()

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObserver()
        setUpBarChart()
    }

    private fun setUpBarChart() {
        binding.apply {
            barChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawLabels(false)
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }

            barChart.axisLeft.apply {
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }
            barChart.axisRight.apply {
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }
            barChart.apply {
                description.text = "Avg Speed Over Time"
                legend.isEnabled = false
            }
        }
    }

    private fun subscribeToObserver() {
        viewModel.totalTimeRun.observe(viewLifecycleOwner, {
            it?.let {
                val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                binding.tvTotalTime.text = totalTimeRun
            }
        })

        viewModel.totalDistance.observe(viewLifecycleOwner, {
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10) / 10f
                val totalDistanceString = "$totalDistance km"
                binding.tvTotalDistance.text = totalDistanceString
            }
        })

        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, {
            it?.let {
                val avgSpeed = round(it * 10f) / 10f
                val avgSpeedString = "$avgSpeed km/h"
                binding.tvAverageSpeed.text = avgSpeedString
            }
        })

        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner, {
            it?.let {
                val totalCalories = "$it kcal"
                binding.tvTotalCalories.text = totalCalories
            }
        })

        viewModel.runsSortedByDate.observe(viewLifecycleOwner, {
            it?.let {
                val allAvgSpeed = it.indices.map { i -> BarEntry(i.toFloat(), it[i].avgSpeedInKMH) }
                val barDataSet = BarDataSet(allAvgSpeed, "Avg Speed Over Time").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }
                binding.barChart.data = BarData(barDataSet)
                binding.barChart.marker =
                    CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                binding.barChart.invalidate()
            }
        })
    }

    override fun onDestroyView() {
        _binding = null // Don't forget to recycle it or memory leaked.
        super.onDestroyView()
    }
}