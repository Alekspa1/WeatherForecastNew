package com.drag0n.weatherf0recastn3w.adapter

import androidx.recyclerview.widget.DiffUtil
import com.drag0n.weatherf0recastn3w.domane.Room.ItemCity

class DiffCallback: DiffUtil.ItemCallback<ItemCity>() {
    override fun areItemsTheSame(oldItem: ItemCity, newItem: ItemCity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemCity, newItem: ItemCity): Boolean {
        return oldItem == newItem
    }


}