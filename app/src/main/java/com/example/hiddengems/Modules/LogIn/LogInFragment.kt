package com.example.hiddengems.Modules.LogIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Model.User
import com.example.hiddengems.Modules.Feed.FeedFragment
import com.example.hiddengems.Modules.SignUp.SignUpFragment
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton


class LogInFragment : Fragment() {

    //initializing log in button and sign up button
    var btnLogIn:MaterialButton ?= null
    var btnSignUp:MaterialButton ?= null

    //initializes fragments  for nav
    var fragmentFeed:  FeedFragment?= null
    var fragmentSignUp: SignUpFragment ?=null
    var fragmentLogIn: LogInFragment ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_in, container, false)

        //setting fragments
        fragmentFeed = FeedFragment()
        fragmentSignUp  = SignUpFragment()
        fragmentLogIn = LogInFragment()

        //setting log in button
        btnLogIn = view.findViewById<MaterialButton>(R.id.btnLogIn)

        //setting log in on log in button click
        btnLogIn?.setOnClickListener(){
            onLogIn()
        }

        //setting sign up button
        btnSignUp = view.findViewById<MaterialButton>(R.id.btnSignUp)

        //setting up sign up on button click
        btnSignUp?.setOnClickListener(){
            onSignUp()
        }

        return view
    }

    //function that takes the user to sign up fragment
    fun onSignUp(){
        fragmentSignUp?.let {
            (activity as MainActivity).displayFragment(it)
        }
    }
    //function that sets current user in Model instance and moves user to homepage
    fun onLogIn(){

        Model.instance.currUser.user = "Billy"
        Model.instance.currUser.bio = "Experienced Traveller"
        Model.instance.currUser.favoriteGems = mutableListOf(0,2)
        Model.instance.currUser.visitedGems =mutableListOf(1)

        fragmentLogIn?.let{
            (activity as MainActivity).removeFragment(it)
        }

        (activity as MainActivity).bottomNavShow()
        fragmentFeed?.let {
            (activity as MainActivity).displayFragment(it)
        }
    }

}