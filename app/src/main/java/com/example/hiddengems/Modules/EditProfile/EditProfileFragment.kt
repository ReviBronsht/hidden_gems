package com.example.hiddengems.Modules.EditProfile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Model.User
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout


class EditProfileFragment : Fragment() {

    //initializing back button
    var btnBack: MaterialButton?= null

    //initializing edit text for input
    var etName: EditText?= null
    var etBio: EditText?= null

    //setting gem parameters as null
    var name:String = ""
    var bio:String = ""

    //initializing save button
    var btnSave:MaterialButton ?= null

    //initializing input layouts
    var tilNameLayout: TextInputLayout?= null
    var tilBioLayout: TextInputLayout?= null

    //initializes user
    var currUser: User ?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        //setting back button
        btnBack = view.findViewById<MaterialButton>(R.id.btnBack)

        //setting on click listener of back button to go back
        btnBack?.setOnClickListener(){
            (activity as MainActivity).goBack()
        }

        //getting user from instance of Model
        currUser = Model.instance.currUser

        //getting edittexts
        etName = view.findViewById<EditText>(R.id.etName)
        etBio = view.findViewById<EditText>(R.id.etBio)

        //setting variables to current gem's variables
        currUser?.let {
            name = it.user
            bio = it.bio
        }

        //setting input fields to show current gem's data
        etName?.setText(name)
        etBio?.setText(bio)

        //setting on text changed listeners of edit text input fields to varibles
        etName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                name = etName?.text.toString()
            }
        })
        etBio?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                bio = etBio?.text.toString()
            }
        })

        //getting save button that adds gem with add gem function if the mode is add
        btnSave = view.findViewById<MaterialButton>(R.id.btnSave)

        btnSave?.setOnClickListener() {
            saveProfile()
        }

        //setting layouts
        tilNameLayout = view.findViewById<TextInputLayout>(R.id.tilNameLayout)
        tilBioLayout = view.findViewById<TextInputLayout>(R.id.tilBioLayout)


        return view
    }


    //clear errors sets all errors to null
    fun clearErrors(){
        tilNameLayout?.error = null
        tilBioLayout?.error = null
    }

    //check errors checks for errors in every field and sets the error or toasts accordingly
    //returns if there were errors or not
    fun checkErrors():Boolean{
        var flag = false

        if(name == "" || name.length > 25){
            tilNameLayout?.error = "Enter a name shorter than 25 characters"
            flag = true
        }

        if(bio.length > 250){
            tilBioLayout?.error = "Bio must be shorter than 250 characters"
            flag = true
        }

        return flag
    }

    //function to save profile
    //first clears past errors
    // calls checkerrors function to set new errors if they exist and checks if there were errors
    //if not, creates new gem from values and adds it to gems at first position
    //clears the form with clearform function
    //puts user in profile with displayfragment function
    fun saveProfile(){

        clearErrors()

        var isErrors = checkErrors()

        if (isErrors == false) {
            //val editedProfile = currUser?.copy(user=name, bio=bio)

            var gems = Model.instance.gems

            for (gem in gems){
                if (gem.user == currUser?.user) {
                    gem.user = name
                }
//                for (comment in gem.comments){
//                    if (comment.user == currUser?.user) {
//                        comment.user = name
//                    }
//                }
            }

            currUser?.user = name
            currUser?.bio = bio

            //clearForm()

            (activity as MainActivity).goBack()
//            fragmentViewGem?.let {
//                (activity as MainActivity).displayFragment(it, arg = editedGem.id.toString())
//            }
        }
    }


}