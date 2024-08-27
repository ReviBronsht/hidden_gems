package com.example.hiddengems.Modules.SignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Modules.Feed.FeedFragment
import com.example.hiddengems.Modules.LogIn.LogInFragment
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton

class SignUpFragment : Fragment() {

    //initializing log in button
    var btnLogIn: MaterialButton?= null

    //initializes fragments  for nav
    var fragmentLogIn:  LogInFragment?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        //setting fragments
        fragmentLogIn = LogInFragment()

        //setting log in button
        btnLogIn = view.findViewById<MaterialButton>(R.id.btnLogIn)

        //setting log in on log in button click
        btnLogIn?.setOnClickListener(){
            onLogIn()
        }

        return view

    }


    //function that takes the user to log in fragment
    fun onLogIn(){
        fragmentLogIn?.let {
            (activity as MainActivity).displayFragment(it)
        }
    }

}