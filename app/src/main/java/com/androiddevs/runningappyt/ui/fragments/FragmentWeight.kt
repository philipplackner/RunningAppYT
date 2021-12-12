package com.supinfo.supfitness.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.supinfo.supfitness.R
import com.supinfo.supfitness.adapter.WeightRVAdapter
import com.supinfo.supfitness.database.WeightDataBase
import com.supinfo.supfitness.database.WeightItems
import com.supinfo.supfitness.other.CustomDialogFragment
import com.supinfo.supfitness.repositories.WeightRepository
import com.supinfo.supfitness.ui.viewmodels.WeightViewModel
import com.supinfo.supfitness.ui.viewmodels.WeightViewModelFactory
import kotlinx.android.synthetic.main.fragment_weight.*

class WeightFragment : Fragment(), WeightRVAdapter.WeightItemsClickInterface {

    lateinit var itemsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var list: List<WeightItems>
    private lateinit var weightRVAdapter: WeightRVAdapter
    private lateinit var weightViewModel: WeightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weight, container, false)
        itemsRV = view.findViewById(R.id.idRVItems)
        addFAB = view.findViewById(R.id.idFABAdd)
        list = ArrayList<WeightItems>()
        weightRVAdapter = WeightRVAdapter(list, this)
        itemsRV.layoutManager = LinearLayoutManager(context)
        itemsRV.adapter = weightRVAdapter
        val weightRepository = WeightRepository(WeightDataBase(context))
        val factory = WeightViewModelFactory(weightRepository)
        weightViewModel = ViewModelProvider(this, factory).get(WeightViewModel::class.java)
        weightViewModel.getAllWeightItems().observe(viewLifecycleOwner, Observer {
            weightRVAdapter.list = it
            weightRVAdapter.notifyDataSetChanged()
        })

        addFAB.setOnClickListener {
            openDialog(weightRepository, factory,weightViewModel, weightRVAdapter)
        }
        return view
    }

    fun openDialog(
        weightRepository: WeightRepository,
        factory: WeightViewModelFactory,
        weightViewModel : WeightViewModel,
        weightRVAdapter : WeightRVAdapter
    ){
        val dialog = CustomDialogFragment(weightRepository, factory,weightViewModel, weightRVAdapter)

        dialog.show(parentFragmentManager, "dialog")


    }
    override fun onItemClick(weightItems: WeightItems) {
        weightViewModel.delete(weightItems)
        weightRVAdapter.notifyDataSetChanged()
        Toast.makeText(activity?.applicationContext, "Item Deleted..", Toast.LENGTH_SHORT).show()
    }
}