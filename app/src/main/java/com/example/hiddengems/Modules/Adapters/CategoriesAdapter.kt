package com.example.hiddengems.Modules.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.content.res.AppCompatResources
import com.example.hiddengems.Model.Category
import com.example.hiddengems.R
import com.google.android.material.button.MaterialButton

//Declaring RecyclerView.Adapter subclass for Categories
class CategoriesAdapter (

    //setting parameters as categorieslist and an instance of OnCategoryClickListener interface
    private var categories:MutableList<Category>,
    private val onItemClickListener: OnCategoryClickListener

):RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>(){
    private lateinit var context: Context

    //CategoriesViewHolderClass holds references to the views for each item
    inner class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var btnCategory:Button?= null
        init {
            btnCategory = itemView.findViewById<MaterialButton>(R.id.btnCategory)
        }
    }

    //OnCreateCiewHolder creates and returns a CategoriesViewHolder for a new item
    //inflates the layout and initializes context from parent.context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {

        context = parent.context

        return CategoriesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_category,
                parent,
                false
            )
        )
    }

    //onBindViewHolder binds data to CategoriesViewHolder
    //finds the current category from list by position
    // uses it to set the button text and icon
    // if position is 0, sets the button initially to appear pressed (default category all)
    //sets button onClickListener to onCategoryClick
    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val currCat = categories[position]
        holder.btnCategory?.text =  currCat.name

        val iconResId = context.resources.getIdentifier(currCat.icon, "drawable", context.packageName)
        (holder.btnCategory as MaterialButton).icon = ContextCompat.getDrawable(context,iconResId)

        if (position == 0){
            holder.btnCategory?.backgroundTintList = AppCompatResources.getColorStateList(context, R.color.secondary)
            holder.btnCategory?.setTextColor(ContextCompat.getColor(context,R.color.white))

        }

        holder.btnCategory?.setOnClickListener(){
            holder.btnCategory?.let {
                onItemClickListener.onCategoryClick(position, it)
            }
        }
    }

    //returns the number of items in categories list so recyclerview determines how many items to display
    override fun getItemCount(): Int {
        return categories.size
    }

    fun setCategories(categories:MutableList<Category>){
        this.categories = categories
        notifyDataSetChanged()
    }


    //interface with onclick function for onclick events to be implemented by classes that need to handle click events for category items
    interface OnCategoryClickListener{
        fun onCategoryClick(position:Int,currBtn:Button)
    }
}
