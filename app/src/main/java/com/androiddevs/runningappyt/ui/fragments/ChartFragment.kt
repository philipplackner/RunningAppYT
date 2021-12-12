package com.androiddevs.runningappyt.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.databinding.FragmentCurveBinding
import com.androiddevs.runningappyt.db.WeightDataBase
import com.androiddevs.runningappyt.ui.viewmodels.ChartViewModel
import com.androiddevs.runningappyt.ui.viewmodels.ChartViewModelFactory
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.Series


class CurveFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentCurveBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_curve, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = WeightDataBase.invoke(application).getWeightDao()
        val viewModelFactory = ChartViewModelFactory(dataSource, application)
        val dashboardViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChartViewModel::class.java)

        //binding.dashboardViewModel = dashboardViewModel
        binding.setLifecycleOwner(this)

        val textView: TextView =  binding.root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val graph: GraphView =  binding.root.findViewById(R.id.graph)
        var series: Series<DataPoint>
        dashboardViewModel.series.observe(viewLifecycleOwner, Observer {
            series = it as Series<DataPoint>
            graph.addSeries(series)
        })





        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setXAxisBoundsManual(true)
        graph.getViewport().setMinX(-13.0)
        graph.getViewport().setMaxX(1.0)

        return binding.root
    }
}