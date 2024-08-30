package com.example.hiddengems.Modules.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Category
import com.example.hiddengems.Model.City
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Modules.Adapters.GemsAdapter
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class SearchFragment : Fragment(), GemsAdapter.OnGemClickListener {

    //initializing edit text for search input
    var etSearch: EditText ?= null
    //initializing autocompletetextview to pick city and type
    var actvCity: MaterialAutoCompleteTextView ?= null
    var actvType: MaterialAutoCompleteTextView ?= null
    //initializing rating sot button
    var btnRatingSort: MaterialButton ?= null
    //initializing gems adapter
    var gemsAdapter: GemsAdapter?= null

    //setting filter and sort parameters as null
    var searchString:String ?= null
    var city:String?= null
    var type:String?= null
    var ratingSort: Boolean ?= null

    //initializing progress bar
    var pbGems:ProgressBar ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_search, container, false)

        //getting search edittext
        etSearch = view.findViewById<EditText>(R.id.etSearch)

        //setting on text changed listener of edit text input field to set search string to text and run filterandsort function
        etSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                searchString = etSearch?.text.toString()
                onFilterAndSortClick()
            }
        })

        //getting autocompletetextview for city, getting all cities from local db,
        // initializing array adapter with context, drop_down_item layout and cities
        // setting adapter of actvCity as new adapter
        actvCity = view.findViewById<MaterialAutoCompleteTextView>(R.id.actvCity)
        val cities = mutableListOf<String>()
        Model.instance.getAllCities { resCities ->
            val tempCities = resCities as MutableList<City>
            for (i in tempCities) {
                cities.add(i.name)
            }
            val citiesAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, cities)
            actvCity?.setAdapter(citiesAdapter)
        }

        // setting on click listener of city items to set city to clicked item, or to null if clicked on All
        actvCity?.setOnItemClickListener{parent, view, position, id ->
            city = cities[position]
            if (city == "All"){
                city = null
            }
            onFilterAndSortClick()
        }

        //getting autocompletetextview for type, getting all types from local db,
        //using for loop to only get their names as strings,
        // initializing array adapter with context, drop_down_item layout and types
        // setting adapter of actvType as new adapter
        actvType = view.findViewById<MaterialAutoCompleteTextView>(R.id.actvType)
        val types: MutableList<String> = mutableListOf()
        Model.instance.getAllCategories { resCategories ->
            val categories = resCategories as MutableList<Category>
            for (i in categories){
                types.add(i.name)
            }
            val typesAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item,types)
            actvType?.setAdapter(typesAdapter)
        }
        // setting on click listener of type items to set type to clicked item, or to null if clicked on All
        actvType?.setOnItemClickListener{parent, view, position, id ->
            type = types[position]
            if (type == "All"){
                type = null
            }
            onFilterAndSortClick()
        }

        //gettinig rating sort button
        btnRatingSort = view.findViewById<MaterialButton>(R.id.btnRatingSort)

        //setting ratingsortbutton to call ratingsort function and filtersort function
        btnRatingSort?.setOnClickListener(){
            onRatingSortClicked()
            onFilterAndSortClick()
        }

        //setting spinner
        pbGems = view.findViewById<ProgressBar>(R.id.pbGems)

        //setting up gems recycler view by getting gems from db, initialising adapter with them, this onclicklistennr and row layout, setting the adapter of recyclerview, and setting layout manager
        //val gems = Model.instance.gems
        Model.instance.getLatestGems { resGems ->
            val gems = resGems as MutableList
            gemsAdapter = GemsAdapter(gems, this, R.layout.layout_gem_row)
            val rvGems = view.findViewById<RecyclerView>(R.id.rvSuggestedGems)
            rvGems.adapter = gemsAdapter
            rvGems.layoutManager = LinearLayoutManager(requireContext())
            pbGems?.visibility = View.GONE
        }


        return view
    }

    //onRatingSortClicked gets value of ratingSort, sets the new value of rating sort after click and sets the correct icon on btnRating sort
    //so click on no sort goes to descending sort, click on descending goes to ascending, click on ascending goes to no sort
    fun onRatingSortClicked(){
        when(ratingSort) {
            null -> {
                ratingSort = true
                context?.let {
                    btnRatingSort?.icon = AppCompatResources.getDrawable(it, R.drawable.sort_up_svgrepo_com)
                }
            }
            true -> {
                ratingSort = false
                context?.let {
                    btnRatingSort?.icon = AppCompatResources.getDrawable(it, R.drawable.sort_down_svgrepo_com)
                }
            }
            else -> {
                ratingSort = null
                context?.let {
                    btnRatingSort?.icon = AppCompatResources.getDrawable(it, R.drawable.sort_svgrepo_com)
                }
            }
        }
    }

    //function that overrides onGemClick from OnGemClickListener
    //calls viewGem function
    override fun onGemClick(position: Int) {
        (activity as MainActivity).viewGem(position)
    }

    //onFilterAndSortClick returns sorted gems using filter and sort query from db and turns spinner on to load and off when loaded
    //updates on adapter
    fun onFilterAndSortClick()
    {
        gemsAdapter?.updateGems(mutableListOf())
        pbGems?.visibility = View.VISIBLE
        Model.instance.filterAndSortGems(searchString,type,city,ratingSort) { filteredGems ->
            gemsAdapter?.updateGems(filteredGems)
            pbGems?.visibility = View.GONE
        }
    }

}