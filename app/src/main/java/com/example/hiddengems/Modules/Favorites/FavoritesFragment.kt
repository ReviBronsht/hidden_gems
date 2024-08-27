package com.example.hiddengems.Modules.Favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hiddengems.MainActivity
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Model.Model
import com.example.hiddengems.Modules.Gems.GemsAdapter
import com.example.hiddengems.R

class FavoritesFragment : Fragment(), GemsAdapter.OnGemClickListener  {
    //initializing gems adapter
    var gemsAdapter: GemsAdapter ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        //setting up gems recycler view by getting gems, filtering them to get the user's favorite gems,
        // initialising adapter with them, this onclicklistennr and row layout, setting the adapter of recyclerview, and setting layout manager
        val gems = Model.instance.gems
        val filteredGems = (activity as MainActivity).filterGemsById(gems,Model.instance.currUser.favoriteGems)
        gemsAdapter = GemsAdapter(filteredGems,this,R.layout.layout_gem_row)
        val rvGems = view.findViewById<RecyclerView>(R.id.rvFavoriteGems)
        rvGems.adapter = gemsAdapter
        rvGems.layoutManager = LinearLayoutManager(requireContext())


        return view
    }

    //function that overrides onGemClick from OnGemClickListener
    //calls viewGem function
    override fun onGemClick(position: Int) {
        (activity as MainActivity).viewGem(position)
    }



}