package com.example.hiddengems.Modules.ViewGem

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Category
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Modules.AddEditGem.AddEditGemFragment
import com.example.hiddengems.Modules.Adapters.CommentsAdapter
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

class ViewGemFragment : Fragment() {

    //getting the gems array from instance of Model
    var gems = Model.instance.gems
    //initialising views that hold Gem data
    var id:String ?= null
    var btnBack: Button?=null
    var tvUserName:TextView?=null
    var tvGemName:TextView?=null
    var tvGemDesc:TextView?=null
    var tvAddress:TextView?=null
    var tvGemRating:TextView?=null
    var tvGemCity:TextView?=null
    var tvGemType:TextView?=null
    var etComment:EditText?=null
    var btnPost: Button?=null
    //initialising views that hold comment related data
    var commentsAdapter: CommentsAdapter?= null
    var rvComments:RecyclerView ?= null
    var tvCommentsNum:TextView ?= null
    var commentsNum:Int ?= null
    //initialising rating button views
    var btnRating1:MaterialButton ?= null
    var btnRating2:MaterialButton ?= null
    var btnRating3:MaterialButton ?= null
    var btnRating4:MaterialButton ?= null
    var btnRating5:MaterialButton ?= null

    //initializing edit button and delete button
    var btnEditGem:MaterialButton ?= null
    var btnDeleteGem:MaterialButton?=null

    //initializing addeditgemfragment
    var fragmentAddEditGem: AddEditGemFragment?= null

    //initializing add/remove gems to faves button
    var ivFavorite: ShapeableImageView?= null

    //initializing add/remove gems to visited button
    var btnVisitedGem:MaterialButton?=null

    var currGem:Gem ?= null
    var currComments:MutableList<Comment> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_gem, container, false)

        view.findViewById<TextView>(R.id.tvHideView).visibility = View.VISIBLE
        view.findViewById<ProgressBar>(R.id.pbViewGem).visibility = View.VISIBLE

        //hide bottom nav - invoked here in case entered from back button
        (activity as MainActivity).bottomNavHide()

        //getting the id of the Gem from argument and finding the gem by id in gems list
        id = arguments?.getString("arg")
       // var currGem: Gem = gems.filter { it.gId == id?.toInt() }[0]
        id?.let {
            Model.instance.getGemById(it) { resGem ->
                currGem = resGem.gem
                if(!resGem.comments.isEmpty()) {
                    currComments = resGem.comments.reversed() as MutableList<Comment>
                }

                //view.findViewById<ProgressBar>(R.id.pbCats).visibility = View.GONE


        //setting back button and setting on click listener to go back using MainActivity functions and display bottom nav
        btnBack = view.findViewById<Button>(R.id.btnBack)

        btnBack?.setOnClickListener(){
            (activity as MainActivity).goBack()
            (activity as MainActivity).bottomNavShow()
        }

        //setting gem data views
        tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        tvGemName = view.findViewById<TextView>(R.id.tvGemName)
        tvGemDesc = view.findViewById<TextView>(R.id.tvGemDesc)
        tvAddress = view.findViewById<TextView>(R.id.tvAddress)
        tvGemRating = view.findViewById<TextView>(R.id.tvGemRating)
        tvGemCity = view.findViewById<TextView>(R.id.tvGemCity)
        tvGemType = view.findViewById<TextView>(R.id.tvGemType)
        //setting text of gem data views from current gem
        tvUserName?.text = currGem?.user
        tvGemName?.text = currGem?.name
        tvGemDesc?.text = currGem?.desc
        tvAddress?.text = currGem?.address
        tvGemRating?.text = currGem?.rating.toString()
        tvGemCity?.text = currGem?.city
        tvGemType?.text = currGem?.type

        //getting comments number view, calculating number of comments, and setting comments number view text to number of comments
        tvCommentsNum = view.findViewById<TextView>(R.id.tvCommentsNum)
        commentsNum = currComments?.size
        tvCommentsNum?.text = "("+ commentsNum.toString() +")"

        //setting up comments recycler view by getting comments, initialising adapter with them, setting the adapter of recyclerview, and setting layout manager
        val comments = currComments
        commentsAdapter = comments?.let { CommentsAdapter(it) }
        rvComments = view.findViewById<RecyclerView>(R.id.rvComments)
        rvComments?.adapter = commentsAdapter
        rvComments?.layoutManager = LinearLayoutManager(requireContext())

        //setting comments input and post comment button
        etComment = view.findViewById<EditText>(R.id.etComment)
        btnPost = view.findViewById<Button>(R.id.btnPost)
        //setting on click listener of post button that gets text from input field, checks if its not empty, and adds comment by calling onPostClicked function
        btnPost?.setOnClickListener(){
            val commentText = etComment?.text.toString()
            if (!commentText.isEmpty()) {
                onPostClicked(Model.instance.currUser.user, commentText)
            }
        }

        //setting on text changed listener of comment input field to disable the button if there is no text, and enable it if there is text
        etComment?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val commentText = etComment?.text.toString()
                if (!commentText.isEmpty()) {
                    btnPost?.setEnabled(true)
                    context?.let {
                    btnPost?.backgroundTintList =
                        AppCompatResources.getColorStateList(it, R.color.secondary)
                    }
                }
                else {
                    btnPost?.setEnabled(false)
                    context?.let {
                        btnPost?.backgroundTintList =
                            AppCompatResources.getColorStateList(it, R.color.darkgray)
                    }
                }
            }
        })

        //setting rating buttons
        btnRating1 = view.findViewById<MaterialButton>(R.id.btnRating1)
        btnRating2 = view.findViewById<MaterialButton>(R.id.btnRating2)
        btnRating3 = view.findViewById<MaterialButton>(R.id.btnRating3)
        btnRating4 = view.findViewById<MaterialButton>(R.id.btnRating4)
        btnRating5 = view.findViewById<MaterialButton>(R.id.btnRating5)

        //creating list of rating buttons to access them by index
        val ratingBtnList:MutableList<MaterialButton?> = mutableListOf(btnRating1,btnRating2,btnRating3,btnRating4,btnRating5)

        // for every rating button, sets its on click listener to rate with rate function and change appearance to rated with markRating function
        for (i in 1..5) {
            ratingBtnList[i-1]?.setOnClickListener() {
                currGem?.let { it1 -> rate(it1, i) }
                markRating(ratingBtnList, i)
            }
        }

        //if user previously rated gem, sets it
        if (currGem?.myRatingIdx != -1){
            currGem?.let {
                val myRating = it.ratings[it.myRatingIdx]
                markRating(ratingBtnList, myRating)
            }
        }

        //if user is Gem's creator, set up edit/delete
        //if not, set up visited
        if(Model.instance.currUser.user == currGem?.user) {
            //setting edit gem button and making it visible
            btnEditGem = view.findViewById<MaterialButton>(R.id.btnEditGem)
            btnEditGem?.visibility = View.VISIBLE

            //setting edit add fragment
            fragmentAddEditGem = AddEditGemFragment()

            //setting on click listener of edit gem button to display addeditgem fragment in edit mode (by passing id as argument)
            btnEditGem?.setOnClickListener() {
                fragmentAddEditGem?.let {
                    (activity as MainActivity).displayFragment(
                        it,
                        arg = currGem?.gId.toString(),
                        savePrevGem = true
                    )
                }
            }

            //setting delete gem button and making it visible
            btnDeleteGem = view.findViewById<MaterialButton>(R.id.btnDeleteGem)
            btnDeleteGem?.visibility = View.VISIBLE

            //setting on click listener of delete gem button to delete gem and go back to homepage
            btnDeleteGem?.setOnClickListener() {
               // gems.remove(currGem)
                currGem?.let {
                    Model.instance.deleteGem(it) {}
                    (activity as MainActivity).goBack()
                }
            }

        }
        else{
            //getting visited button and setting it to visible
            btnVisitedGem = view.findViewById<MaterialButton>(R.id.btnVisitedGem)
            btnVisitedGem?.visibility = View.VISIBLE

            //setting visited button to add or remove visited gem on click
            btnVisitedGem?.setOnClickListener(){
                currGem?.let { it1 -> addRemoveVisited(it1.gId) }
            }

            //setting appearance of visited if gem is in faves
            if (currGem?.gId in Model.instance.currUser.visitedGems){
                btnVisitedGem?.text = "Visited!"
                context?.let {
                    btnVisitedGem?.backgroundTintList =
                        AppCompatResources.getColorStateList(it, R.color.secondary)
                }
            }
        }
        //getting fave button
        ivFavorite = view.findViewById<ShapeableImageView>(R.id.ivFavorite)

        //setting fave button to add or remove fave gem on click
        ivFavorite?.setOnClickListener(){
            currGem?.let { it1 -> addRemoveFave(it1.gId) }
        }

        //setting icon of fave if gem is in faves
        if (currGem?.gId in Model.instance.currUser.favoriteGems){
            ivFavorite?.setImageResource(R.drawable.heart_svgrepo_com)
        }

                view.findViewById<TextView>(R.id.tvHideView).visibility = View.GONE
                view.findViewById<ProgressBar>(R.id.pbViewGem).visibility = View.GONE

            }
        }
        return view
    }

    //function that adds or removes id of gem to/from user's visited gems and sets text accordingly
    fun addRemoveVisited(id:Int){
        val currUser = Model.instance.currUser
        if (id in currUser.visitedGems){
            currUser.visitedGems.remove(id)
            btnVisitedGem?.text = "Not visited"
            context?.let {
                btnVisitedGem?.backgroundTintList =
                    AppCompatResources.getColorStateList(it, R.color.darkgray)
            }
        }
        else {
            currUser.visitedGems.add(0,id)
            btnVisitedGem?.text = "Visited!"
            context?.let {
                btnVisitedGem?.backgroundTintList =
                    AppCompatResources.getColorStateList(it, R.color.secondary)
            }
        }
    }

    //function that adds or removes id of gem to/from user's favorite gems and sets icon accordingly
    fun addRemoveFave(id:Int){
        val currUser = Model.instance.currUser
        if (id in currUser.favoriteGems){
            currUser.favoriteGems.remove(id)
            ivFavorite?.setImageResource(R.drawable.heart_alt_svgrepo_com)
        }
        else {
            currUser.favoriteGems.add(0,id)
            ivFavorite?.setImageResource(R.drawable.heart_svgrepo_com)
        }
    }

    // on post clicked function creates a comment from passed user and text,
    // adds comment with commentsAdapter function
    // setts text of comments input field to null
    // scrolls to top of comments recycler view
    // culculates new comments num and sets the relevant view to display it
    fun onPostClicked(user:String,commentText:String){
        val newComment: Comment? = id?.toInt()?.let { Comment(it,user,commentText) }
        if (newComment != null) {
            Model.instance.insertComment(newComment) {}
            commentsAdapter?.addComment(newComment)
        }
        etComment?.text = null
        rvComments?.scrollToPosition(0)
        commentsNum = commentsNum?.plus(1)
        tvCommentsNum?.text = "("+ commentsNum.toString() +")"

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

    //rate function gets new rating, updates rating of passed gem and of rating view, and updates the gem at index of gems list
    fun rate(gem:Gem, newRating:Int){
        val gemIndex = gems.indexOfFirst { it == gem }
        //gem.updateRating(newRating)
        val (updatedRating, updatedMyRatingIdx, updatedRatings) = (activity as MainActivity).updateRating(newRating, gem.myRatingIdx, gem.ratings)

        val updatedGem = gem.copy(rating = updatedRating, myRatingIdx = updatedMyRatingIdx, ratings = updatedRatings)

        Model.instance.upsertGem(updatedGem){}

        tvGemRating?.text = updatedRating.toString()
        //gems[gemIndex] = gem

    }



}