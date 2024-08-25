package com.example.hiddengems.Modules.ViewGem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Model
import com.example.hiddengems.R

class ViewGemFragment : Fragment() {
    var id:String ?= null
    var btnBack: Button?=null
    var tvUserName:TextView?=null
    var tvGemName:TextView?=null
    var tvGemDesc:TextView?=null
    var tvAddress:TextView?=null
    var tvGemRating:TextView?=null
    var tvGemCity:TextView?=null
    var tvGemType:TextView?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_gem, container, false)

        id = arguments?.getString("arg")
        var gems = Model.instance.gems
        var currGem: Gem = gems.filter { it.id == id?.toInt() }[0]

        btnBack = view.findViewById<Button>(R.id.btnBack)

        btnBack?.setOnClickListener(){
            (activity as MainActivity).goBack()
            (activity as MainActivity).bottomNavShow()
        }

        tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        tvGemName = view.findViewById<TextView>(R.id.tvGemName)
        tvGemDesc = view.findViewById<TextView>(R.id.tvGemDesc)
        tvAddress = view.findViewById<TextView>(R.id.tvAddress)
        tvGemRating = view.findViewById<TextView>(R.id.tvGemRating)
        tvGemCity = view.findViewById<TextView>(R.id.tvGemCity)
        tvGemType = view.findViewById<TextView>(R.id.tvGemType)

        tvUserName?.text = currGem.user
        tvGemName?.text = currGem.name
        tvGemDesc?.text = currGem.desc
        tvAddress?.text = currGem.address
        tvGemRating?.text = currGem.rating.toString()
        tvGemCity?.text = currGem.city
        tvGemType?.text = currGem.type


        return view
    }


}