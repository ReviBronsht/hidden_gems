package com.example.hiddengems.Modules.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Modules.EditProfile.EditProfileFragment
import com.example.hiddengems.Modules.Gems.GemsAdapter
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() , GemsAdapter.OnGemClickListener{

    //initializing gems adapters
    var visitedGemsAdapter: GemsAdapter ?= null
    var myGemsAdapter: GemsAdapter ?= null

    //getting current user
    val currUser = Model.instance.currUser

    //initializing textviews that hold the user's info
    var tvUserName:TextView ?= null
    var tvBio:TextView ?= null

    //initializing edit profile button
    var btnEditProfile:MaterialButton ?= null

    //initializing edit profile fragment
    var fragmentEditProfile: EditProfileFragment?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        //setting user info text views
        tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        tvBio = view.findViewById<TextView>(R.id.tvBio)

        //setting text views' text to user info
        tvUserName?.text = currUser.user
        tvBio?.text = currUser.bio

        val gems = Model.instance.gems

        //if visited gems isn't empty, setting up visited gems
        if(currUser.visitedGems.size != 0) {
            //setting up gems recycler view by getting gems, filtering them to get the user's favorite gems,
            // initialising adapter with them, this onclicklistennr and row layout, setting the adapter of recyclerview, and setting layout manager
            val visitedGems = (activity as MainActivity).filterGemsById(gems, currUser.visitedGems)
            visitedGemsAdapter = GemsAdapter(visitedGems, this, R.layout.layout_gem_square)
            val rvVisitedGems = view.findViewById<RecyclerView>(R.id.rvVisitedGems)
            rvVisitedGems.adapter = visitedGemsAdapter
            rvVisitedGems.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        //setting up gems recycler view by getting gems, filtering them to get the user's favorite gems,
        // initialising adapter with them, this onclicklistennr and row layout, setting the adapter of recyclerview, and setting layout manager
        val myGems = (activity as MainActivity).filterGemsByUser(gems,currUser.user)
        myGemsAdapter = GemsAdapter(myGems,this,R.layout.layout_gem_row)
        val rvMyGems = view.findViewById<RecyclerView>(R.id.rvMyGems)
        rvMyGems.adapter = myGemsAdapter
        rvMyGems.layoutManager = LinearLayoutManager(requireContext())


        //setting edit profile button
        btnEditProfile = view.findViewById<MaterialButton>(R.id.btnEditProfile)

        //setting edit profile fragment
        fragmentEditProfile = EditProfileFragment()

        //setting onclick of edit profile button to go to edit profile
        btnEditProfile?.setOnClickListener(){
            (activity as MainActivity).bottomNavHide()
            fragmentEditProfile?.let {
                (activity as MainActivity).displayFragment(it, savePrev = true)
            }
        }
        return view
    }

    //function that overrides onGemClick from OnGemClickListener
    //calls viewGem function
    override fun onGemClick(position: Int) {
        (activity as MainActivity).viewGem(position)
    }

}