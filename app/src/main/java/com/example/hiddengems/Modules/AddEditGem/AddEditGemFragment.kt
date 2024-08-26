package com.example.hiddengems.Modules.AddEditGem

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Modules.Feed.FeedFragment
import com.example.hiddengems.Modules.Gems.GemsAdapter
import com.example.hiddengems.Modules.ViewGem.ViewGemFragment
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout

class AddEditGemFragment : Fragment() {

    //boolean to check if fragment is in edit mode or add mode
    var isEditMode = false

    //initializing edit text for search input
    var etName: EditText?= null
    var etDesc: EditText?= null
    var etAddress: EditText?= null
    //initializing autocompletetextview to pick city and type
    var actvCity: MaterialAutoCompleteTextView?= null
    var actvType: MaterialAutoCompleteTextView?= null
    //initialising rating button views
    var btnRating1:MaterialButton ?= null
    var btnRating2:MaterialButton ?= null
    var btnRating3:MaterialButton ?= null
    var btnRating4:MaterialButton ?= null
    var btnRating5:MaterialButton ?= null

    //setting gem parameters as null
    var name:String = ""
    var desc:String = ""
    var address:String = ""
    var city:String= ""
    var type:String= ""
    var rating: Int = -1

    //initializing save button
    var btnSave:MaterialButton ?= null

    //initializing input layouts
    var tilNameLayout: TextInputLayout?= null
    var tilDescLayout: TextInputLayout?= null
    var tilAddressLayout: TextInputLayout?= null
    var tilCityLayout: TextInputLayout?= null
    var tilTypeLayout: TextInputLayout?= null

    //intializing cities and types to hold lists of cities and types
    var cities:MutableList<String> = mutableListOf()
    var types:MutableList<String> = mutableListOf()

    //initializes list of rating buttons
    var ratingBtnList:MutableList<MaterialButton?> = mutableListOf()

    //initializes fragments for navigation
    var fragmentFeed:  FeedFragment?= null
    var fragmentViewGem: ViewGemFragment?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_edit_gem, container, false)

        //trying to get the id of the Gem from argument
        //if exists, edit mode, if not, add mode
        val id = arguments?.getString("arg")
        if (id != null)
        {
            isEditMode = true
        }

        //getting edittexts
        etName = view.findViewById<EditText>(R.id.etName)
        etDesc = view.findViewById<EditText>(R.id.etDesc)
        etAddress = view.findViewById<EditText>(R.id.etAddress)

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
        etDesc?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                desc = etDesc?.text.toString()
            }
        })
        etAddress?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                address = etAddress?.text.toString()
            }
        })

        //getting autocompletetextview for city, getting all cities from instance of Model,
        // initializing array adapter with context, drop_down_item layout and cities
        // setting adapter of actvCity as new adapter
        actvCity = view.findViewById<MaterialAutoCompleteTextView>(R.id.actvCity)
        cities = Model.instance.cities.toMutableList()
        cities.removeFirst() //removing first "All" item
        val citiesAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item,cities)
        actvCity?.setAdapter(citiesAdapter)

        // setting on click listener of city items to set city to clicked item
        actvCity?.setOnItemClickListener{parent, view, position, id ->
            city = cities[position]
        }

        //getting autocompletetextview for type, getting all types from instance of Model,
        //using for loop to only get their names as strings,
        // initializing array adapter with context, drop_down_item layout and types
        // setting adapter of actvType as new adapter
        actvType = view.findViewById<MaterialAutoCompleteTextView>(R.id.actvType)
        val categories = Model.instance.categories
        for (i in categories){
            types.add(i.name)
        }
        types.removeFirst()//removing first "All" item
        val typesAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item,types)
        actvType?.setAdapter(typesAdapter)

        // setting on click listener of type items to set type to clicked item, or to null if clicked on All
        actvType?.setOnItemClickListener{parent, view, position, id ->
            type = types[position]
        }


        //setting rating buttons
        btnRating1 = view.findViewById<MaterialButton>(R.id.btnRating1)
        btnRating2 = view.findViewById<MaterialButton>(R.id.btnRating2)
        btnRating3 = view.findViewById<MaterialButton>(R.id.btnRating3)
        btnRating4 = view.findViewById<MaterialButton>(R.id.btnRating4)
        btnRating5 = view.findViewById<MaterialButton>(R.id.btnRating5)

        //creating list of rating buttons to access them by index
        ratingBtnList = mutableListOf(btnRating1,btnRating2,btnRating3,btnRating4,btnRating5)

        // for every rating button, sets its on click listener to rate with rate function and change appearance to rated with markRating function
        for (i in 1..5) {
            ratingBtnList[i-1]?.setOnClickListener() {
                markRating(ratingBtnList, i)
                rating = i
            }
        }

        //getting save button that adds gem with add gem function
        btnSave = view.findViewById<MaterialButton>(R.id.btnSave)
        btnSave?.setOnClickListener(){
            addGem()
        }

        //setting layouts
        tilNameLayout = view.findViewById<TextInputLayout>(R.id.tilNameLayout)
        tilDescLayout = view.findViewById<TextInputLayout>(R.id.tilDescLayout)
        tilAddressLayout = view.findViewById<TextInputLayout>(R.id.tilAddressLayout)
        tilCityLayout = view.findViewById<TextInputLayout>(R.id.tilCityLayout)
        tilTypeLayout = view.findViewById<TextInputLayout>(R.id.tilTypeLayout)

        //setting fragments
        fragmentFeed = FeedFragment()
        fragmentViewGem = ViewGemFragment()

        //check if edit mode, and make relevant adjustments
        if (isEditMode){
            //getting title text view and setting title to edit
            val tvAddEditTitle = view.findViewById<TextView>(R.id.tvAddEditTitle)
            tvAddEditTitle.text = "Edit Gem"
           // tvAddEditTitle.height = 20


            //getting back button and givint it go back function on click listener
            val btnBack = view.findViewById<MaterialButton>(R.id.btnBack)
            btnBack.visibility=View.VISIBLE

            btnBack.setOnClickListener(){
                fragmentViewGem?.let {
                    (activity as MainActivity).displayFragment(it, arg = id)
                }
            }
        }

        return view
    }

    //clear form resets variables, sets fields to empty text, and clears rating
    fun clearForm(){
        name = ""
        desc = ""
        address = ""
        city = ""
        type = ""
        rating = -1

        etName?.text=null
        etDesc?.text=null
        etAddress?.text=null
        actvCity?.text = null
        actvType?.text = null

        markRating(ratingBtnList,0)

    }

    //clear errors sets all errors to null
    fun clearErrors(){
        tilNameLayout?.error = null
        tilDescLayout?.error = null
        tilAddressLayout?.error = null
        tilCityLayout?.error = null
        tilTypeLayout?.error = null
    }

    //check errors checks for errors in every field and sets the error or toasts accordingly
    //returns if there were errors or not
    fun checkErrors():Boolean{
        var flag = false

        if(name == "" || name.length > 25){
            tilNameLayout?.error = "Enter a name shorter than 25 characters"
            flag = true
        }

        if(desc == "" || name.length > 250){
            tilDescLayout?.error = "Enter a description shorter than 250 characters"
            flag = true
        }

        if(address == "" || name.length > 25){
            tilAddressLayout?.error = "Enter an address shorter than 30 characters"
            flag = true
        }

        if (city !in cities){
            tilCityLayout?.error = "Pick city from list"
            flag = true
        }

        if (type !in types){
            tilTypeLayout?.error = "Pick type from list"
            flag = true
        }

        if(rating == -1){
            Toast.makeText(context,"Please rate your gem",Toast.LENGTH_SHORT).show()
        }

        return flag
    }

    //function to add gem
    //first clears past errors
    // calls checkerrors function to set new errors if they exist and checks if there were errors
    //if not, creates new gem from values and adds it to gems at first position
    //clears the form with clearform function
    //puts user in homepage with displayfragment function
    fun addGem(){

        clearErrors()

        var isErrors = checkErrors()

        if (isErrors == false) {

            val gems = Model.instance.gems

            val newGem: Gem = Gem(
                gems.size, "Billy", name, desc, address, city, type, rating.toDouble(),
                mutableListOf<Int>(rating)
            )

            gems.add(0,newGem)

            clearForm()

            fragmentFeed?.let {
                (activity as MainActivity).displayFragment(it)
            }
        }
    }

    //mark rating function first makes all rating buttons look unclicked,
    // then makes all rating buttons til current rating look clicked
    fun markRating(buttons:MutableList<MaterialButton?>,rating:Int){

        for (i in 0..4) {
            val button = buttons[i]
            context?.let {
                button?.iconTint =
                    AppCompatResources.getColorStateList(it, R.color.darkgray)
            }
        }

        for (i in 0..rating-1) {
            val button = buttons[i]
            context?.let {
                button?.iconTint =
                    AppCompatResources.getColorStateList(it, R.color.accent)
            }
        }
    }

}