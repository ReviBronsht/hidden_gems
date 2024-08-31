package com.example.hiddengems.Modules.EditProfile

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Model.User
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso


class EditProfileFragment : Fragment() {

    //initializing back button
    var btnBack: MaterialButton?= null

    //initializing edit text for input
    var etName: EditText?= null
    var etBio: EditText?= null

    //setting profile parameters as null
    var name:String = ""
    var bio:String = ""
    var image:String = ""

    //initializing save button
    var btnSave:MaterialButton ?= null

    //initializing input layouts
    var tilNameLayout: TextInputLayout?= null
    var tilBioLayout: TextInputLayout?= null

    //initializes user
    var currUser: User ?= null

    //initializes edit icon button and image view
    var btnEditImg: MaterialButton ?= null
    var ivUserImg: ShapeableImageView?= null

    //declaring launcher
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    //boolean if image was changed
    var wasImageChanged:Boolean = false

    //registers launcher on create
    override fun onCreate(savedInstanceState: Bundle?) {
        registerResult()
        super.onCreate(savedInstanceState)
    }
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
            image = it.image
        }

        //setting input fields to show current gem's data and current uploaded image
        etName?.setText(name)
        etBio?.setText(bio)

        //loading image if exists
        if (image!="" && ivUserImg != null) {
            Picasso.with(context).load(image).fit().centerCrop().into(ivUserImg)
        }


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


        //setting edit icon button and image view
        btnEditImg = view.findViewById<MaterialButton>(R.id.btnEditImg)
        ivUserImg = view.findViewById<ShapeableImageView>(R.id.ivUserImg)

        //setting on click of edit icon button
        btnEditImg?.setOnClickListener(){
            pickImageGallery()
        }


        return view
    }

    //launches to pick images with intent
    fun pickImageGallery(){
        var intent:Intent= Intent(MediaStore.ACTION_PICK_IMAGES)
        resultLauncher.launch(intent)
    }

    //registering launcher result to set the image and mark it was changed
   fun registerResult(){
        resultLauncher=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                var imageUri:Uri? = it.data?.data
                if (imageUri != null){
                    ivUserImg?.setImageURI(imageUri)
                    wasImageChanged = true
                }

            }
        )

   }

//    fun uploadImage(){
//        ivUserImg?.isDrawingCacheEnabled = true
//        ivUserImg?.buildDrawingCache()
//        val bitmap = (ivUserImg?.drawable as BitmapDrawable).bitmap
//        val imgName = currUser?.uId.toString()+"ProfileImg"
//        Model.instance.uploadImage(imgName,bitmap){url->
//            if(url != null) {
//
//                image = url.toString()
//            }
//        }
//    }

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
    //checks if user by the same name exists, if it is shows error, if not continues
    // calls checkerrors function to set new errors if they exist and checks if there were errors
    //if not, edits user in local db
    //clears the form with clearform function
    //puts user in profile with displayfragment function
    //if user changed the image, uploads it and saves user with new image
    fun saveProfile() {

        clearErrors()


        Model.instance.getUserByName(name) { foundUser ->
            if (foundUser != null && name != Model.instance.currUser.user) {
                tilNameLayout?.error = "This user already exists"
            } else {


                var isErrors = checkErrors()

                if (isErrors == false) {
                    if (wasImageChanged) {
                        ivUserImg?.isDrawingCacheEnabled = true
                        ivUserImg?.buildDrawingCache()
                        val bitmap = (ivUserImg?.drawable as BitmapDrawable).bitmap
                        val imgName = currUser?.uId.toString() + "ProfileImg"
                        Model.instance.uploadImage(imgName, bitmap) { url ->
                            if (url != null) {
                                image = url.toString()
                                val editedUser =
                                    currUser?.copy(user = name, bio = bio, image = image)


                                editedUser?.let {
                                    Model.instance.upsertUser(editedUser, oldId = currUser?.uId) {
                                        currUser?.user = name
                                        currUser?.bio = bio
                                        currUser?.image = image

                                        //clearForm()

                                        (activity as MainActivity).goBack()
                                    }
                                }
                            }
                        }
                    } else {
                        val editedUser = currUser?.copy(user = name, bio = bio)


                        editedUser?.let {
                            Model.instance.upsertUser(editedUser) {
                                currUser?.user = name
                                currUser?.bio = bio

                                //clearForm()

                                (activity as MainActivity).goBack()
                            }
                        }
                    }
                }
            }
        }
    }
    }


