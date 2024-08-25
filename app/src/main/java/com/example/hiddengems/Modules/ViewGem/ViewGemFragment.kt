package com.example.hiddengems.Modules.ViewGem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.hiddengems.R

class ViewGemFragment : Fragment() {
    var id:String ?= null
    var tvTest: TextView?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_gem, container, false)

        id = arguments?.getString("arg")

        tvTest = view.findViewById<TextView>(R.id.tvTest)
        tvTest?.text = id


        return view
    }
}