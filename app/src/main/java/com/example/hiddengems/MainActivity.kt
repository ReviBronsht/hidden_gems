package com.example.hiddengems

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.hiddengems.Modules.AddEditGem.AddEditGemFragment
import com.example.hiddengems.Modules.Favorites.FavoritesFragment
import com.example.hiddengems.Modules.Feed.FeedFragment
import com.example.hiddengems.Modules.Profile.ProfileFragment
import com.example.hiddengems.Modules.Search.SearchFragment

class MainActivity : AppCompatActivity() {

    //initializes fragments
    var fragmentFeed:  FeedFragment?= null
    var fragmentSearch:  SearchFragment?= null
    var fragmentAddEditGem: AddEditGemFragment?= null
    var fragmentFavorites: FavoritesFragment?= null
    var fragmentProfile: ProfileFragment?= null
    //initializes nav buttons
    var btnHome: Button ?= null
    var btnSearch: Button ?= null
    var btnAddGem: Button ?= null
    var btnFaves: Button ?= null
    var btnProfile: Button ?= null

    var llBottomNav:LinearLayout?=null
    //initializes var to keep current displayed fragment
    var inDisplayFragment: Fragment?= null
    //initializes var to keep current displayed button
    var inDisplayButton: Button?= null
    var prevFragment: Fragment ?=null

    //normally back would remove first fragment (feed), overrides to close instead of backing when its the last fragment left in the stack
    //function also selects the correct button to be currently highlighted, based on currently displayed fragment after back
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
            println(inDisplayFragment)
            val currFragmentId = supportFragmentManager.findFragmentById(R.id.flMainFragmentContainer)?.toString()?.split("{")
            currFragmentId?.let {
                when (it[0]) {
                    "FeedFragment" -> btnHome?.let {button->
                        displayButton(button)
                    }
                    "SearchFragment" -> btnSearch?.let {button->
                        displayButton(button)
                    }
                    "AddEditGemFragment" -> btnAddGem?.let {button->
                        displayButton(button)
                    }
                    "FavoritesFragment" -> btnFaves?.let {button->
                        displayButton(button)
                    }
                    else -> btnProfile?.let {button->
                        displayButton(button)
                    }
                }
            }
        } else {
            //if (supportFragmentManager.backStackEntryCount != 1)
            //{supportFragmentManager.popBackStackImmediate()}
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //setting fragments
        fragmentFeed = FeedFragment()
        fragmentSearch = SearchFragment()
        fragmentAddEditGem = AddEditGemFragment()
        fragmentFavorites = FavoritesFragment()
        fragmentProfile = ProfileFragment()
        //fragmentProfile = FeedFragment.newInstance("Five")

        //settting nav buttons
        btnHome = findViewById(R.id.btnHome)
        btnSearch = findViewById(R.id.btnSearch)
        btnAddGem = findViewById(R.id.btnAddGem)
        btnFaves = findViewById(R.id.btnFaves)
        btnProfile = findViewById(R.id.btnProfile)

        llBottomNav = findViewById<LinearLayout>(R.id.llBottomNav)

        //setting onclicklistener that displays fragments when nav buttons are pressed
        btnHome?.setOnClickListener{
            fragmentFeed?.let {fragment ->
                btnHome?.let {button->
                    displayFragment(fragment, button)
                }
            }
        }
        btnSearch?.setOnClickListener{
            fragmentSearch?.let {fragment ->
                btnSearch?.let {button->
                    displayFragment(fragment, button)
                }
            }
        }
        btnAddGem?.setOnClickListener{
            fragmentAddEditGem?.let {fragment ->
                btnAddGem?.let {button->
                    displayFragment(fragment, button)
                }
            }
        }
        btnFaves?.setOnClickListener{
            fragmentFavorites?.let {fragment ->
                btnFaves?.let {button->
                    displayFragment(fragment, button)
                }
            }
        }
        btnProfile?.setOnClickListener{
            fragmentProfile?.let {fragment ->
                btnProfile?.let {button->
                    displayFragment(fragment, button)
                }
            }
        }

        fragmentFeed?.let {fragment ->
            btnHome?.let {button->
                displayFragment(fragment, button)
            }
        }

//        gemsAdapter = GemsAdapter(mutableListOf())
//
//        val rvGems = findViewById<RecyclerView>(R.id.rvGems)
//        val tvSaveBtn = findViewById<TextView>(R.id.tvSaveBtn)
//        val etName = findViewById<EditText>(R.id.etName)
//        val etDesc = findViewById<EditText>(R.id.etDesc)
//        val etAddress= findViewById<EditText>(R.id.etAddress)
//        val etRating = findViewById<EditText>(R.id.etRating)
//        val etCity= findViewById<EditText>(R.id.etCity)
//        val etType = findViewById<EditText>(R.id.etType)
//
//
//
//
//        rvGems.adapter = gemsAdapter
//        rvGems.layoutManager = LinearLayoutManager(this)
//
//        tvSaveBtn.setOnClickListener{
//            val gemName = etName.text.toString()
//            val gemDesc = etDesc.text.toString()
//            val gemAddress = etAddress.text.toString()
//            val gemRating = etRating.text.toString()
//            val gemCity = etCity.text.toString()
//            val gemType = etType.text.toString()
//            val gem = Gem(gemName,gemDesc,gemAddress,gemCity,gemType,gemRating.toInt())
//            gemsAdapter.addGem(gem)
//        }


    }

//    fun onButtonClicked(view: View){
//
//        if(feedFragment == null) {
//            displayFragment()
//        }
//    }
//    fun displayFragment(){
//        feedFragment= FeedFragment.newInstance("Billy")
//
//        feedFragment?.let {fragment ->
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.add(R.id.flMainFragmentContainer, fragment)
//            transaction.addToBackStack("TAG")
//            transaction.commit()
//        }
//    }


    internal fun bottomNavShow(){
        llBottomNav?.visibility=View.VISIBLE
        btnHome?.visibility = View.VISIBLE
        btnSearch?.visibility = View.VISIBLE
        btnAddGem?.visibility = View.VISIBLE
        btnFaves?.visibility = View.VISIBLE
        btnProfile?.visibility = View.VISIBLE
    }
    internal fun bottomNavHide(){
        llBottomNav?.visibility =View.GONE
        btnHome?.visibility = View.GONE
        btnSearch?.visibility = View.GONE
        btnAddGem?.visibility = View.GONE
        btnFaves?.visibility = View.GONE
        btnProfile?.visibility = View.GONE
    }
    internal fun goBack(){
        prevFragment?.let {
            displayFragment(it)
        }
    }

    //function that removes current fragment, displays new fragment, and sets it as new current fragment
    //if button is passed, uses displayButton function to set it as current tab
    //if an argument is passed, uses Bundle to put it to new fragment
    internal fun displayFragment(fragment: Fragment, button: Button? = inDisplayButton,arg:String?=null,savePrev:Boolean=false){
        val b = Bundle()
        if (arg != null){
            b.putString("arg",arg)
        }
        fragment.arguments = b

        val transaction = supportFragmentManager.beginTransaction()

        if(savePrev){
            prevFragment = inDisplayFragment
        }

        inDisplayFragment?.let {
            transaction.remove(it)
        }

        transaction.add(R.id.flMainFragmentContainer, fragment)
        transaction.addToBackStack("TAG")
        inDisplayFragment = fragment

        //recoloring selected button
        button?.let {
            displayButton(it)
        }
        transaction.commit()
    }

    //function that removes fragment
    fun removeFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.addToBackStack("TAG")
        transaction.commit()
    }

    //function that displays the current selected button
    fun displayButton(button: Button){
        inDisplayButton?.let {
            it.backgroundTintList = getColorStateList(R.color.main)
        }
        button.backgroundTintList = getColorStateList(R.color.secondary)
        inDisplayButton = button

    }
}

