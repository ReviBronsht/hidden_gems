package com.example.hiddengems.Modules.Gems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.hiddengems.Model.Gem
import com.example.hiddengems.Modules.Categories.CategoriesAdapter
import com.example.hiddengems.R
import com.google.android.material.imageview.ShapeableImageView

//Declaring RecyclerView.Adapter subclass for Gems
class GemsAdapter (

    //setting parameters as gems list and an instance of OnGemClickListener interface
    private var gems:MutableList<Gem>,
    private val onItemClickListener: OnGemClickListener

):RecyclerView.Adapter<GemsAdapter.GemsViewHolder>(){

    //CategoriesViewHolderClass holds references to the views for each item
    inner class GemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var tvUserName:TextView?=null
        var tvGemName:TextView?= null
        var tvGemRating:TextView?=null
        var tvGemCity:TextView?=null
        var tvGemType:TextView?=null
        var ivGemImg: ShapeableImageView?=null

        init {
            tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
            tvGemName = itemView.findViewById<TextView>(R.id.tvGemName)
            tvGemRating = itemView.findViewById<TextView>(R.id.tvGemRating)
            tvGemCity = itemView.findViewById<TextView>(R.id.tvGemCity)
            tvGemType = itemView.findViewById<TextView>(R.id.tvGemType)
            ivGemImg = itemView.findViewById<ShapeableImageView>(R.id.ivGemImg)
        }
    }

    //OnCreateViewHolder creates and returns a GemsViewHolder for a new item
    //inflates the layout and initializes context from parent.context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GemsViewHolder {
        return GemsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_gem,
                parent,
                false
            )
        )
    }

//    fun addGem(gem: Gem){
//        gems.add(gem)
//        notifyItemInserted(gems.size-1)
//    }


    //onBindViewHolder binds data to GemsViewHolder
    //finds the current gem from list by position
    // uses it to set the name, user, rating, city and type
    //sets onClickListener to onGemClick
    override fun onBindViewHolder(holder: GemsViewHolder, position: Int) {
        val currGem = gems[position]
        holder.tvUserName?.text =  currGem.user
        holder.tvGemName?.text =  currGem.name
        holder.tvGemRating?.text =  currGem.rating.toString()
        holder.tvGemCity?.text =  currGem.city
        holder.tvGemType?.text =  currGem.type

    holder.ivGemImg?.setOnClickListener(){
            onItemClickListener.onGemClick(currGem.id)
        }
    }

    //returns the number of items in categories list so recyclerview determines how many items to display
    override fun getItemCount(): Int {
        return gems.size
    }

    // update the list and notify changes
    fun updateGems(newGems: List<Gem>) {
        gems = newGems.toMutableList()
        notifyDataSetChanged()  // Notify the adapter that the data has changed
    }

    //interface with onclick function for onclick events to be implemented by classes that need to handle click events for gem items
    interface OnGemClickListener{
        fun onGemClick(position:Int)
    }
}
