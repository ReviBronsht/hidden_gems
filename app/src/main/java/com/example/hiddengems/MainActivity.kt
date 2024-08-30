package com.example.hiddengems

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Display.Mode
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Model.relationships.GemWithUser
import com.example.hiddengems.Modules.AddEditGem.AddEditGemFragment
import com.example.hiddengems.Modules.EditProfile.EditProfileFragment
import com.example.hiddengems.Modules.Favorites.FavoritesFragment
import com.example.hiddengems.Modules.Feed.FeedFragment
import com.example.hiddengems.Modules.LogIn.LogInFragment
import com.example.hiddengems.Modules.Profile.ProfileFragment
import com.example.hiddengems.Modules.Search.SearchFragment
import com.example.hiddengems.Modules.SignUp.SignUpFragment
import com.example.hiddengems.Modules.ViewGem.ViewGemFragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    //initializes fragments
    var fragmentFeed:  FeedFragment?= null
    var fragmentSearch:  SearchFragment?= null
    var fragmentAddEditGem: AddEditGemFragment?= null
    var fragmentFavorites: FavoritesFragment?= null
    var fragmentProfile: ProfileFragment?= null
    var fragmentViewGem: ViewGemFragment?=null
    var fragmentEditProfile: EditProfileFragment?=null
    var fragmentLogIn: LogInFragment ?=null
    var fragmentSignUp: SignUpFragment ?= null

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
    //intilializes var to keep previous displayed fragment
    var prevFragment: Fragment ?=null
    //initializes var to keep previous gem id if going back from editGem
    var prevGem:String ?=null

    //override on back pressed function to use custom back navigation
    //if in home page or log in, exits the app
    //if not in homepage or log in, uses GoBack function for custom back navigation
    //if in sign up, goes back to log in
    override fun onBackPressed() {
//        bottomNavShow()
//        if (inDisplayFragment == fragmentViewGem) {
//            goBack()
//        }
//        else{
//            if (supportFragmentManager.backStackEntryCount > 1) {
//                super.onBackPressed()
//                println(inDisplayFragment)
//                val currFragmentId =
//                    supportFragmentManager.findFragmentById(R.id.flMainFragmentContainer)
//                        ?.toString()?.split("{")
//                currFragmentId?.let {
//                    when (it[0]) {
//                        "FeedFragment" -> btnHome?.let { button ->
//                            displayButton(button)
//                        }
//
//                        "SearchFragment" -> btnSearch?.let { button ->
//                            displayButton(button)
//                        }
//
//                        "AddEditGemFragment" -> btnAddGem?.let { button ->
//                            displayButton(button)
//                        }
//
//                        "FavoritesFragment" -> btnFaves?.let { button ->
//                            displayButton(button)
//                        }
//
//                        else -> btnProfile?.let { button ->
//                            displayButton(button)
//                        }
//                    }
//                }
        if (Model.instance.currUser.user == ""){
            if (inDisplayFragment == fragmentLogIn) {
                finish()
            }
            else{
                fragmentLogIn?.let{
                    displayFragment(it)
                }
            }
        }
        else {
            if (inDisplayFragment != fragmentFeed) {
                goBack(prevGem)
            } else {
                //if (supportFragmentManager.backStackEntryCount != 1)
                //{supportFragmentManager.popBackStackImmediate()}
                finish()
                if (false) {
                    super.onBackPressed()
                }

                // }
            }
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
        fragmentViewGem = ViewGemFragment()
        fragmentEditProfile = EditProfileFragment()
        fragmentLogIn = LogInFragment()
        fragmentSignUp = SignUpFragment()
        //fragmentProfile = FeedFragment.newInstance("Five")

        //setting nav buttons
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

        //if user not logged in, sets log in
        // if user  logged in, sets feed
        if (Model.instance.currUser.user != "") {
            fragmentFeed?.let { fragment ->
                btnHome?.let { button ->
                    displayFragment(fragment, button)
                }
            }
        }
        else {
            bottomNavHide()
            fragmentLogIn?.let {
                displayFragment(it)
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


    //function that recalculates rating when user adds rating from all previous ratings and new rating average
    //if user previously rated gem (has index of rating), updates at index, if doesn't, adds new rating
    fun updateRating(nRating: Int,myRatingIdx:Int,ratings:MutableList<Int>):Triple<Double,Int,MutableList<Int>>{
        var newMyRatingIdx = myRatingIdx
        if (myRatingIdx == -1) {
            ratings.add(nRating)
            newMyRatingIdx = ratings.size-1
        }
        else {
            ratings[myRatingIdx] = nRating
        }
        var rating = ratings.average().toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble()
        return Triple(rating,newMyRatingIdx,ratings)
    }

    //function that filters gems by ids and maps them to order of ids in faves list
    fun filterGemsById(gems: MutableList<Gem>, ids: List<Int>): MutableList<Gem> {
        val idOrderMap = ids.withIndex().associate { it.value to it.index }
        return gems
            .filter { it.gId in idOrderMap }
            .sortedBy { idOrderMap[it.gId] } as MutableList<Gem>
    }

    fun filterGemsByUser(gems: MutableList<GemWithUser>, user:String): MutableList<GemWithUser> {
        return gems
            .filter { it.user.user == user } as MutableList<GemWithUser>
    }

    //viewGem function calls displayFragment function from MainActivity to display ViewGem fragment
    //sends the position (which contains relevant gem id) as argument
    fun viewGem(position:Int){
        fragmentViewGem?.let {
            bottomNavHide()
            displayFragment(it, arg = position.toString(),savePrev = true)
        }
    }

    // function that shows the bottom nav for fragments that require it
    internal fun bottomNavShow(){
        llBottomNav?.visibility=View.VISIBLE
        btnHome?.visibility = View.VISIBLE
        btnSearch?.visibility = View.VISIBLE
        btnAddGem?.visibility = View.VISIBLE
        btnFaves?.visibility = View.VISIBLE
        btnProfile?.visibility = View.VISIBLE
    }
    // function that hides the bottom nav for fragments that don't require it
    internal fun bottomNavHide(){
        llBottomNav?.visibility =View.GONE
        btnHome?.visibility = View.GONE
        btnSearch?.visibility = View.GONE
        btnAddGem?.visibility = View.GONE
        btnFaves?.visibility = View.GONE
        btnProfile?.visibility = View.GONE
    }

    //custom back navigation function
    //if in edit gem page (currPrevGem passed), goes back to view the gem with that id
    //if in view gem page, which can be accessed from multiple pages, goes back to previous fragment
    //if in edit profile, go back to profile
    //if in other pages that aren't homepage, goes back to homepage
    //if set to turn on home button, does (for log in page)
    internal fun goBack(currPrevGem:String?=null){
        println(inDisplayFragment)
        if (currPrevGem!=null)
        {
            fragmentViewGem?.let {
                displayFragment(it, arg = currPrevGem)
                prevGem = null
            }
        }
        else if(prevFragment != null){
            prevFragment?.let {
                displayFragment(it)
                bottomNavShow()
                prevFragment  = null
            }
        }
        else
        fragmentFeed?.let {
            fragmentFeed?.let {
                displayFragment(it, btnHome)
            }
        }
    }

    //function that removes current fragment, displays new fragment, and sets it as new current fragment
    //if button is passed, uses displayButton function to set it as current tab
    //if an argument is passed, uses Bundle to put it to new fragment
    //if savePrev is true, saves previous fragment
    //if savePrevGem is true, saves gem id in current passed argument
    internal fun displayFragment(fragment: Fragment, button: Button? = inDisplayButton,arg:String?=null,savePrev:Boolean=false,savePrevGem:Boolean=false,displayHomeButton:Boolean=false){
        println(inDisplayFragment)
        val b = Bundle()
        if (arg != null){
            b.putString("arg",arg)
        }
        fragment.arguments = b

        val transaction = supportFragmentManager.beginTransaction()

        if(savePrev){
            prevFragment = inDisplayFragment
        }
        if(savePrevGem){
            prevGem=arg
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

        if (displayHomeButton){
            btnHome?.let { displayButton(it) }
        }
        transaction.commit()
        println(inDisplayFragment)
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

