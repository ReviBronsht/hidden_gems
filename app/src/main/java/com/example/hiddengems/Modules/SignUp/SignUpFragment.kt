package com.example.hiddengems.Modules.SignUp

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Model.User
import com.example.hiddengems.Modules.Feed.FeedFragment
import com.example.hiddengems.Modules.LogIn.LogInFragment
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout

class SignUpFragment : Fragment() {

    //initializing log in button
    var btnLogIn: MaterialButton?= null

    //initializes fragments  for nav
    var fragmentLogIn:  LogInFragment?= null

    //initializing edit text for input
    var etEmail: EditText?= null
    var etName: EditText?= null
    var etPassword: EditText?= null
    var etConfPassword: EditText?= null

    //setting sign up parameters as empty
    var email:String = ""
    var name:String = ""
    var password:String = ""
    var confPassword:String = ""

    //initializing sign up button
    var btnSignUp:MaterialButton ?= null

    //initializing input layouts
    var tilEmailLayout: TextInputLayout?= null
    var tilNameLayout: TextInputLayout?= null
    var tilPasswordLayout: TextInputLayout?= null
    var tilConfPasswordLayout: TextInputLayout?= null

    //initializing success message text view
    var tvSucessMsg:TextView?=null

    //initializing sign up spinner
    var pbSignUp:ProgressBar?=null

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

        //setting edit text for input
        etEmail = view.findViewById(R.id.etEmail)
        etName= view.findViewById(R.id.etName)
        etPassword= view.findViewById(R.id.etPassword)
        etConfPassword= view.findViewById(R.id.etConfPassword)

        //setting success message text view
        tvSucessMsg = view.findViewById(R.id.tvSucessMsg)

        //setting spinner
        pbSignUp = view.findViewById(R.id.pbSignUp)

        //setting sign up button
        btnSignUp= view.findViewById(R.id.btnSignUp)

        //setting sign up button on click listener
        btnSignUp?.setOnClickListener(){
            onSignUp()
        }

        //initializing input layouts
        tilEmailLayout = view.findViewById(R.id.tilEmailLayout)
        tilNameLayout = view.findViewById(R.id.tilNameLayout)
        tilPasswordLayout = view.findViewById(R.id.tilPasswordLayout)
        tilConfPasswordLayout = view.findViewById(R.id.tilConfPasswordLayout)

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
        etName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                name = etName?.text.toString()
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
        etConfPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                confPassword = etConfPassword?.text.toString()
            }
        })

        return view

    }

    //clear errors sets all errors to null
    fun clearErrors(){
        tilNameLayout?.error = null
        tilEmailLayout?.error = null
        tilPasswordLayout?.error = null
        tilConfPasswordLayout?.error = null
    }


    //check errors checks for errors in every field and sets the error
    //returns if there were errors or not
    fun checkErrors():Boolean{
        var flag = false

        if(email == "" || email.length > 25 || (activity as MainActivity).checkEmail(email) == false){
            tilEmailLayout?.error = "Enter a valid email shorter than 25 characters"
            flag = true
        }

        if(name == "" || name.length > 25){
            tilNameLayout?.error = "Enter a name shorter than 25 characters"
            flag = true
        }

        if(confPassword != password){
            tilConfPasswordLayout?.error="passwords don't match."
            tilPasswordLayout?.error="passwords don't match."
            flag = true
        }
        if(password == "" || password.length > 25 || password.length < 6){
            tilPasswordLayout?.error = "Enter a password between 6 and 25 characters"
            flag = true
        }

        if(confPassword == "" || confPassword.length > 25 || confPassword.length < 6){
            tilConfPasswordLayout?.error = "Enter a password between 6 and 25 characters"
            flag = true
        }

        return flag

    }

    //function to sign up
    //first clears past errors
    //checks if user by the same name exists, if it is shows error, if not continues
    // calls checkerrors function to set new errors if they exist and checks if there were errors
    //if not, creates a new user with name and email, and does sign up from model
    fun onSignUp() {

        clearErrors()


        Model.instance.getUserByName(name) { foundUser ->
            if (foundUser != null ) {
                tilNameLayout?.error = "This user already exists"
            } else {
                pbSignUp?.visibility = View.VISIBLE

                var isErrors = checkErrors()

                if (isErrors == false) {

                    var newUser = User(name,email=email)
                    Model.instance.doSignUp(newUser,email,password){
                        clearForm()
                        tvSucessMsg?.visibility = View.VISIBLE
                        pbSignUp?.visibility= View.GONE
                    }


            }
        }
    }
}

    //function that clears the form
    fun clearForm(){
        name = ""
        email = ""
        password = ""
        confPassword = ""

        etName?.text=null
        etEmail?.text=null
        etPassword?.text=null
        etConfPassword?.text = null

    }
    //function that takes the user to log in fragment
    fun onLogIn(){
        fragmentLogIn?.let {
            (activity as MainActivity).displayFragment(it)
        }
    }

}