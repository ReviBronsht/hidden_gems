package com.example.hiddengems.Modules.LogIn

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Model.User
import com.example.hiddengems.Modules.Feed.FeedFragment
import com.example.hiddengems.Modules.SignUp.SignUpFragment
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout


class LogInFragment : Fragment() {

    //initializing log in button and sign up button
    var btnLogIn:MaterialButton ?= null
    var btnSignUp:MaterialButton ?= null

    //initializes fragments  for nav
    var fragmentFeed:  FeedFragment?= null
    var fragmentSignUp: SignUpFragment ?=null
    var fragmentLogIn: LogInFragment ?= null

    //initializing edit text for input
    var etEmail: EditText?= null
    var etPassword: EditText?= null

    //setting sign up parameters as empty
    var email:String = ""
    var password:String = ""

    //initializing input layouts
    var tilEmailLayout: TextInputLayout?= null
    var tilPasswordLayout: TextInputLayout?= null

    //initializing error message text view
    var tvErrMsg: TextView ?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_in, container, false)

        //setting edit text for input
        etEmail = view.findViewById(R.id.etEmail)
        etPassword= view.findViewById(R.id.etPassword)

        //initializing input layouts
        tilEmailLayout = view.findViewById(R.id.tilEmailLayout)
        tilPasswordLayout = view.findViewById(R.id.tilPasswordLayout)

        //setting error message text view
        tvErrMsg = view.findViewById(R.id.tvErrMsg)

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

        //setting on text changed listeners of edit text input fields to varibles
        etEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                email = etEmail?.text.toString()
            }
        })
        etPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                password = etPassword?.text.toString()
            }
        })
        return view
    }

    //clear errors sets all errors to null
    fun clearErrors(){
        tilEmailLayout?.error = null
        tilPasswordLayout?.error = null
    }


    //check errors checks for errors in every field and sets the error
    //returns if there were errors or not
    fun checkErrors():Boolean{
        var flag = false

        if(email == "" || email.length > 25 || (activity as MainActivity).checkEmail(email) == false){
            tilEmailLayout?.error = "Enter a valid email shorter than 25 characters"
            flag = true
        }

        if(password == "" || password.length > 25 || password.length < 6){
            tilPasswordLayout?.error = "Enter a password between 6 and 25 characters"
            flag = true
        }
        return flag

    }
    //function that takes the user to sign up fragment
    fun onSignUp(){
        fragmentSignUp?.let {
            (activity as MainActivity).displayFragment(it)
        }
    }
    //function that gets the user by id from db, sets current user in Model instance and moves user to homepage
    fun onLogIn(){

        tvErrMsg?.visibility = View.GONE
        clearErrors()

        var isErrors = checkErrors()

        if (isErrors == false) {

            Model.instance.doLogIn(email, password) {resUser ->
                if (resUser != null){
                    Model.instance.currUser.uId = resUser.uId
                    Model.instance.currUser.email = resUser.email
                    Model.instance.currUser.user = resUser.user
                    Model.instance.currUser.bio = resUser.bio
                    Model.instance.currUser.image = resUser.image
                    Model.instance.currUser.favoriteGems = resUser.favoriteGems
                    Model.instance.currUser.visitedGems = resUser.visitedGems


                    fragmentLogIn?.let {
                        (activity as MainActivity).removeFragment(it)
                    }


                    (activity as MainActivity).bottomNavShow()
                    fragmentFeed?.let {
                        (activity as MainActivity).displayFragment(it, displayHomeButton = true)
                    }

                }
                else{
                    tvErrMsg?.visibility = View.VISIBLE
                }
            }
        }

    }

}