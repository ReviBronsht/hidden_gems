package com.example.hiddengems

import android.os.Bundle
import android.widget.Button
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

private lateinit var gemsAdapter: GemsAdapter

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
    //initializes var to keep current displayed fragment
    var inDisplayFragment: Fragment?= null

    //normally back would remove first fragment (feed), overrides to close instead of backing when its the last fragment left in the stack
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            //println(supportFragmentManager.backStackEntryCount)
            //supportFragmentManager.popBackStackImmediate()
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

        //setting onclicklistener that displays fragments when nav buttons are pressed
        btnHome?.setOnClickListener{
            fragmentFeed?.let {
                displayFragment(it)
            }
        }
        btnSearch?.setOnClickListener{
            fragmentSearch?.let {
                displayFragment(it)
            }
        }
        btnAddGem?.setOnClickListener{
            fragmentAddEditGem?.let {
                displayFragment(it)
            }
        }
        btnFaves?.setOnClickListener{
            fragmentFavorites?.let {
                displayFragment(it)
            }
        }
        btnProfile?.setOnClickListener{
            fragmentProfile?.let {
                displayFragment(it)
            }
        }

        fragmentFeed?.let {
            displayFragment(it)
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

    //function that removes current fragment, displays new fragment, and sets it as new current fragment
    fun displayFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        inDisplayFragment?.let {
            transaction.remove(it)
        }
        transaction.add(R.id.flMainFragmentContainer, fragment)
        transaction.addToBackStack("TAG")
        inDisplayFragment = fragment
        transaction.commit()
    }

    //function that removes fragment
    fun removeFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.addToBackStack("TAG")
        transaction.commit()
    }
}