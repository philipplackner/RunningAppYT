package com.supinfo.supfitness.other

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.supinfo.supfitness.R
import com.supinfo.supfitness.adapter.WeightRVAdapter
import com.supinfo.supfitness.database.WeightItems
import com.supinfo.supfitness.repositories.WeightRepository
import com.supinfo.supfitness.ui.viewmodels.WeightViewModel
import com.supinfo.supfitness.ui.viewmodels.WeightViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class CustomDialogFragment(
    weightRepository: WeightRepository,
    factory: WeightViewModelFactory,
    weightViewModel : WeightViewModel,
    weightRVAdapter : WeightRVAdapter
) : DialogFragment(){


    var weightRVAdapter: WeightRVAdapter = weightRVAdapter
    var weightViewModel : WeightViewModel = weightViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_custom_dialog, container, false)

        val increaseBtn = rootView.findViewById<ImageView>(R.id.ivIncrease)
        val decreaseBtn = rootView.findViewById<ImageView>(R.id.ivDecrease)

        val cancelBtn = rootView.findViewById<Button>(R.id.idBtnCancel)
        val addBtn = rootView.findViewById<Button>(R.id.idBtnAdd)
        val itemQuantityEdt = rootView.findViewById<EditText>(R.id.idEdtItemQuantity)

        itemQuantityEdt.setText("80")
        itemQuantityEdt.requestFocus()
        cancelBtn.setOnClickListener {
            dismiss()
        }
        addBtn.setOnClickListener {
            val itemName: String = "2021-12-10"
            //val itemName : String = SimpleDateFormat("yyyy-MM-dd").format(Date())
            val itemQuantity: String = itemQuantityEdt.text.toString()
            val qty : Int = itemQuantity.toInt()
            /*val randomValues: Int = kotlin.random.Random.nextInt(70,80)
            val qty: Int = randomValues*/
            if(itemName.isNotEmpty() && itemQuantity.isNotEmpty()) {
                val items = WeightItems(itemName, qty )
                weightViewModel.insert(items)
                Toast.makeText(activity?.applicationContext,"Item Inserted..", Toast.LENGTH_SHORT).show()
                weightRVAdapter.notifyDataSetChanged()
                dismiss()
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    "Please enter all data..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        increaseBtn.setOnClickListener {
            itemQuantityEdt.setText("${
                itemQuantityEdt.text.toString().toInt() + 1
            }")
        }

        decreaseBtn.setOnClickListener {
            itemQuantityEdt.setText("${
                itemQuantityEdt.text.toString().toInt() - 1
            }")
        }

        return rootView
    }
}