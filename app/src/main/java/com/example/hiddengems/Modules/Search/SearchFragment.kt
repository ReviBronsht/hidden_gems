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
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Modules.Gems.GemsAdapter
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class SearchFragment : Fragment(),GemsAdapter.OnGemClickListener {

    //initializing edit text for search input
    var etSearch: EditText ?= null
    //initializing autocompletetextview to pick city and type
    var actvCity: MaterialAutoCompleteTextView ?= null
    var actvType: MaterialAutoCompleteTextView ?= null
    //initializing rating sot button
    var btnRatingSort: MaterialButton ?= null
    //initializing gems adapter
    var gemsAdapter: GemsAdapter ?= null

    //setting filter and sort parameters as null
    var searchString:String ?= null
    var city:String?= null
    var type:String?= null
    var ratingSort: Boolean ?= null

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

        //getting autocompletetextview for city, getting all cities from instance of Model, 
        // initializing array adapter with context, drop_down_item layout and cities
        // setting adapter of actvCity as new adapter
        actvCity = view.findViewById<MaterialAutoCompleteTextView>(R.id.actvCity)
        val cities = Model.instance.cities
        val citiesAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item,cities)
        actvCity?.setAdapter(citiesAdapter)

        // setting on click listener of city items to set city to clicked item, or to null if clicked on All
        actvCity?.setOnItemClickListener{parent, view, position, id ->
            city = cities[position]
            if (city == "All"){
                city = null
            }
            onFilterAndSortClick()
        }

        //getting autocompletetextview for type, getting all types from instance of Model, 
        //using for loop to only get their names as strings,
        // initializing array adapter with context, drop_down_item layout and types
        // setting adapter of actvType as new adapter
        actvType = view.findViewById<MaterialAutoCompleteTextView>(R.id.actvType)
        val categories = Model.instance.categories
        val types: MutableList<String> = mutableListOf()
        for (i in categories){
            types.add(i.name)
        }
        val typesAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item,types)
        actvType?.setAdapter(typesAdapter)

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


        //setting up gems recycler view by getting gems, initialising adapter with them, this onclicklistennr and row layout, setting the adapter of recyclerview, and setting layout manager
        val gems = Model.instance.gems
        gemsAdapter = GemsAdapter(gems,this,R.layout.layout_gem_row)
        val rvGems = view.findViewById<RecyclerView>(R.id.rvSuggestedGems)
        rvGems.adapter = gemsAdapter
        rvGems.layoutManager = LinearLayoutManager(requireContext())


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

    //onFilterAndSortClick gets gems from instance of Model, filters and sorts them with filterAndSortGems and filter and sort parameters
    //updates on adapter
    fun onFilterAndSortClick()
    {
        val originalGems = Model.instance.gems
        val filteredGems = filterAndSortGems(originalGems,searchString,type, city,ratingSort)
        gemsAdapter?.updateGems(filteredGems)
    }
    
    //filterAndSortGems gets list of gems and filter and sort parameters
    //if filter parameter is not null, filters by it and ignores case
    //if sort parameter is true sorts by descending, if its false sorts by ascending, if its null, doesn't sort
    fun filterAndSortGems(gems: MutableList<Gem>, searchString: String? = null, type: String? = null, city: String? = null, ratingSort: Boolean? = null
    ): List<Gem> {

        return gems
            .filter { gem ->
                (searchString == null || gem.name.contains(searchString, ignoreCase = true)) &&
                        (type == null || gem.type.equals(type, ignoreCase = true)) &&
                        (city == null || gem.city.equals(city, ignoreCase = true))
            }
            .let { filteredGems ->
                when (ratingSort) {
                    true -> filteredGems.sortedByDescending { gem -> gem.rating }
                    false -> filteredGems.sortedBy { gem -> gem.rating }
                    null -> filteredGems
                }
            }
    }

}