package com.example.hiddengems.Modules.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Model.relationships.GemWithUser
import com.example.hiddengems.Modules.EditProfile.EditProfileFragment
import com.example.hiddengems.Modules.Adapters.GemsAdapter
import com.example.hiddengems.Modules.LogIn.LogInFragment
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() , GemsAdapter.OnGemClickListener{

    //initializing gems adapters
    var visitedGemsAdapter: GemsAdapter?= null
    var myGemsAdapter: GemsAdapter?= null

    //getting current user
    val currUser = Model.instance.currUser

    //initializing textviews that hold the user's info
    var tvUserName:TextView ?= null
    var tvBio:TextView ?= null

    //initializing edit profile button
    var btnEditProfile:MaterialButton ?= null

    //initializing edit profile fragment
    var fragmentEditProfile: EditProfileFragment?=null

    //initializing log out button
    var btnLogOut: MaterialButton ?= null

    //initializes log in fragment
    var fragmentLogIn: LogInFragment ?= null

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


        //if visited gems isn't empty, setting up visited gems
        //if found favorite gems or if there are none, makes spinner gone
        if(currUser.visitedGems.size != 0) {
            //setting up gems recycler view by getting gems from db based on user's favorite gems ids,
            // initialising adapter with them, this onclicklistennr and row layout, setting the adapter of recyclerview, and setting layout manager
            Model.instance.getGemsInIds(Model.instance.currUser.visitedGems) { resGems ->
                val visitedGems = resGems as MutableList
                visitedGemsAdapter = GemsAdapter(visitedGems, this, R.layout.layout_gem_square)
                val rvVisitedGems = view.findViewById<RecyclerView>(R.id.rvVisitedGems)
                rvVisitedGems.adapter = visitedGemsAdapter
                rvVisitedGems.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                view.findViewById<ProgressBar>(R.id.pbVisitedGems).visibility = View.GONE
            }
        }
        else{
            view.findViewById<ProgressBar>(R.id.pbVisitedGems).visibility = View.GONE
        }

        //setting up gems recycler view by getting gems that user created from db
        // initialising adapter with them, this onclicklistennr and row layout, setting the adapter of recyclerview, and setting layout manager
        //setting spinner to gone
        Model.instance.getGemsOfUser(currUser.uId.toString()) { resUserWithGems ->
            val myGems = mutableListOf<GemWithUser>()
            for (i in resUserWithGems.gems)
            {
                myGems.add(GemWithUser(i,currUser))
            }
            myGemsAdapter = GemsAdapter(myGems,this,R.layout.layout_gem_row)
            val rvMyGems = view.findViewById<RecyclerView>(R.id.rvMyGems)
            rvMyGems.adapter = myGemsAdapter
            rvMyGems.layoutManager = LinearLayoutManager(requireContext())

            view.findViewById<ProgressBar>(R.id.pbMyGems).visibility = View.GONE
        }


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

        //setting log out button
        btnLogOut = view.findViewById<MaterialButton>(R.id.btnLogOut)

        //setting log in fragment
        fragmentLogIn = LogInFragment()

        //setting log out button to log out on click
        btnLogOut?.setOnClickListener(){
            onLogOut()
        }
        return view
    }

    //function that removes current user details in Model instance and moves user to log in
    fun onLogOut(){

        Model.instance.currUser.uId = -1
        Model.instance.currUser.user = ""
        Model.instance.currUser.bio = ""
        Model.instance.currUser.favoriteGems = mutableListOf()
        Model.instance.currUser.visitedGems =mutableListOf()

        (activity as MainActivity).bottomNavHide()
        fragmentLogIn?.let {
            (activity as MainActivity).displayFragment(it)
        }
    }

    //function that overrides onGemClick from OnGemClickListener
    //calls viewGem function
    override fun onGemClick(position: Int) {
        (activity as MainActivity).viewGem(position)
    }

}