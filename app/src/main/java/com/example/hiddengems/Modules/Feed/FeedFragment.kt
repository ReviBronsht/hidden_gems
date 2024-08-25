package com.example.hiddengems.Modules.Feed

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Modules.Categories.CategoriesAdapter
import com.example.hiddengems.Modules.Gems.GemsAdapter
import com.example.hiddengems.Modules.Profile.ProfileFragment
import com.example.hiddengems.Modules.Search.SearchFragment
import com.example.hiddengems.Modules.ViewGem.ViewGemFragment
import com.example.hiddengems.R

// Feed/Home fragment that shows recent gems, lets user filter them by category, and lets user view their details by clicking on them
//declaring class and implementing OnCategoryClickListener and OnGemClickListener interfaces from relevant adapters
class FeedFragment : Fragment(), CategoriesAdapter.OnCategoryClickListener,GemsAdapter.OnGemClickListener{

    //initialising views
    var prevbtn: Button ?= null
    var gemsAdapter: GemsAdapter ?= null
    var fragmentFeed:  FeedFragment?= null
    var fragmentProfile: ProfileFragment?= null
    var fragmentViewGem:  ViewGemFragment?= null

  //    var categories: MutableList<Category> ?= null
//    var tvFeedTitle: TextView ?= null
//    var title:String?= null
//
//    companion object {
//
//        const val TITLE = "TITLE"
//        fun newInstance(title:String) =
//            FeedFragment().apply {
//                arguments = Bundle().apply {
//                    putString(TITLE,title)
//                }
//            }
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let{
//            title = it.getString(TITLE)
//        }
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_feed, container, false)

//        tvFeedTitle = view.findViewById(R.id.tvFeedTitle)
//        tvFeedTitle?.text = title ?: "default value"

    //viewgem fragment to access gem details
    fragmentViewGem = ViewGemFragment()

    //setting up categories recycler view by getting categories, initialising adapter with them and this onclicklistennr, setting the adapter of recyclerview, and setting layout manager
    val categories = Model.instance.categories ?: mutableListOf()
    val catsAdapter = CategoriesAdapter(categories,this)
    val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)
    rvCategories.adapter = catsAdapter
    rvCategories.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

    //setting up gems recycler view by getting categories, initialising adapter with them and this onclicklistennr, setting the adapter of recyclerview, and setting layout manager
    val gems = Model.instance.gems
    gemsAdapter = GemsAdapter(gems,this)
    val rvGems = view.findViewById<RecyclerView>(R.id.rvLatestGems)
    rvGems.adapter = gemsAdapter
    rvGems.layoutManager = LinearLayoutManager(requireContext())

//        val btn1stCategory = view?.findViewById<Button>(R.id.btnCategory)
//        btn1stCategory?.text = "hoi"




    return view
    }

    //overriding onCategoryClick of OnCategoryClickListener to call function that sets current category
    override fun onCategoryClick(position: Int,currBtn: Button) {
        setCurrCat(Model.instance.categories[position].name,currBtn)
    }

    //function to set the current category
    //function calls function that filters and updates recycler view
    //changes pressed category button to look pressed and previous pressed button to look unpressed
    //if there is no previous button, meaning default "All" category was 'selected', changes it to look unpressed
    fun setCurrCat(category:String,button: Button){
        filterAndUpdateRecyclerView(category)

        if (prevbtn == null){
            val btn1stCategory = view?.findViewById<Button>(R.id.btnCategory)
            btn1stCategory?.let{cbtn ->
                context?.let {
                    cbtn.backgroundTintList = getColorStateList(it, R.color.white)
                    cbtn.setTextColor(ContextCompat.getColor(it,R.color.black))
                }
            }

        }
        prevbtn?.let{pbtn ->
            context?.let {
                pbtn.backgroundTintList = getColorStateList(it, R.color.white)
                pbtn.setTextColor(ContextCompat.getColor(it,R.color.black))
            }
        }
        context?.let {
            button.backgroundTintList = getColorStateList(it, R.color.secondary)
            button.setTextColor(ContextCompat.getColor(it,R.color.white))
        }
        prevbtn = button

    }

    //function that overrides onGemClick from OnGemClickListener
    //calls viewGem function
     override fun onGemClick(position: Int) {
        viewGem(position)
    }

    //viewGem function calls displayFragment function from MainActivity to display ViewGem fragment
    //sends the position (which contains relevant gem id) as argument
    //sets prev button to null so it resets when leaving feed fragment
    fun viewGem(position:Int){
        fragmentViewGem?.let {
            prevbtn = null
            (activity as MainActivity).displayFragment(it, arg = position.toString())
        }
        }

    //filter and update rcycler view filters gems from Model instance by calling filterGemsByType function
    fun filterAndUpdateRecyclerView(type: String) {
        val originalGems = Model.instance.gems
        val filteredGems = filterGemsByType(originalGems, type)
        gemsAdapter?.updateGems(filteredGems)
    }

    //filterGemsByType filters gems by passed type
    //unless type is All, in which case returns all gems
    fun filterGemsByType(gems: MutableList<Gem>, type: String): List<Gem> {
        return if (type == "All") {
            gems
        } else {
            gems.filter { it.type == type }
        }
    }
}

