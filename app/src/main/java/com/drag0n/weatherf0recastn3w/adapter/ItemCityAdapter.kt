package com.drag0n.weatherf0recastn3w.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drag0n.weatherf0recastn3w.Const
import com.drag0n.weatherf0recastn3w.domane.Room.ItemCity
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.databinding.ItemCityBinding

class ItemCityAdapter(private val onClickListener: onClick): ListAdapter<ItemCity, ItemCityAdapter.ViewHolder>(DiffCallback()) {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemCityBinding.bind(view)
        fun bind(itemCity: ItemCity, onClick: onClick){
            binding.bCityItemCity.text = itemCity.name
            binding.bCityItemCity.setOnClickListener {
                onClick.onClick(itemCity, Const.SEARCH_CITY)
            }
            binding.imDelItemCity.setOnClickListener {
                onClick.onClick(itemCity, Const.DELETE_CITY)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }
    interface onClick {
        fun onClick(itemCity: ItemCity, action: String)
    }
}