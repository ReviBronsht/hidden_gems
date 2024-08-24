package com.example.hiddengems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView


class GemsAdapter (
    private val gems:MutableList<Gem>
):RecyclerView.Adapter<GemsAdapter.GemsViewHolder>(){
    inner class GemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var tvGemName:TextView?= null
        var tvGemRating:TextView?=null
        var tvGemCity:TextView?=null
        var tvGemType:TextView?=null

        init {
            tvGemName = itemView.findViewById<TextView>(R.id.tvGemName)
            tvGemRating = itemView.findViewById<TextView>(R.id.tvGemRating)
            tvGemCity = itemView.findViewById<TextView>(R.id.tvGemCity)
            tvGemType = itemView.findViewById<TextView>(R.id.tvGemType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GemsViewHolder {
        return GemsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_gem,
                parent,
                false
            )
        )
    }

    fun addGem(gem: Gem){
        gems.add(gem)
        notifyItemInserted(gems.size-1)
    }
    override fun onBindViewHolder(holder: GemsViewHolder, position: Int) {
        val currGem = gems[position]
        holder.tvGemName?.text =  currGem.name
        holder.tvGemRating?.text =  currGem.rating.toString()
        holder.tvGemCity?.text =  currGem.city
        holder.tvGemType?.text =  currGem.type

    }

    override fun getItemCount(): Int {
        return gems.size
    }
}
