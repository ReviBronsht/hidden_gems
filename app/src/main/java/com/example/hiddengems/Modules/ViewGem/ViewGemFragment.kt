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
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Comment
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Modules.Comments.CommentsAdapter
import com.example.hiddengems.Modules.Gems.GemsAdapter
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton

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
    var commentsAdapter:CommentsAdapter ?= null
    var rvComments:RecyclerView ?= null
    var tvCommentsNum:TextView ?= null
    var commentsNum:Int ?= null
    //initialising rating button views
    var btnRating1:MaterialButton ?= null
    var btnRating2:MaterialButton ?= null
    var btnRating3:MaterialButton ?= null
    var btnRating4:MaterialButton ?= null
    var btnRating5:MaterialButton ?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_gem, container, false)

        //getting the id of the Gem from argument and finding the gem by id in gems list
        id = arguments?.getString("arg")
        var currGem: Gem = gems.filter { it.id == id?.toInt() }[0]

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
        tvUserName?.text = currGem.user
        tvGemName?.text = currGem.name
        tvGemDesc?.text = currGem.desc
        tvAddress?.text = currGem.address
        tvGemRating?.text = currGem.rating.toString()
        tvGemCity?.text = currGem.city
        tvGemType?.text = currGem.type

        //getting comments number view, calculating number of comments, and setting comments number view text to number of comments
        tvCommentsNum = view.findViewById<TextView>(R.id.tvCommentsNum)
        commentsNum = currGem.comments.size
        tvCommentsNum?.text = "("+ commentsNum.toString() +")"

        //setting up comments recycler view by getting comments, initialising adapter with them, setting the adapter of recyclerview, and setting layout manager
        val comments = currGem.comments
        commentsAdapter = CommentsAdapter(comments)
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
                onPostClicked("Billy", commentText)
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
                rate(currGem, i)
                markRating(ratingBtnList, i)
            }
        }

        return view
    }

    // on post clicked function creates a comment from passed user and text,
    // adds comment with commentsAdapter function
    // setts text of comments input field to null
    // scrolls to top of comments recycler view
    // culculates new comments num and sets the relevant view to display it
    fun onPostClicked(user:String,commentText:String){
        val newComment: Comment = Comment(user,commentText)
        commentsAdapter?.addComment(newComment)
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
        gem.updateRating(newRating)
        tvGemRating?.text = gem.rating.toString()
        gems[gemIndex] = gem

    }



}