package com.jamesclark.android.androidsampleapp.viewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floor
import com.jamesclark.android.androidlibraryexample.app.databinding.FloorListItemBinding
import java.util.function.Consumer

class FloorRecyclerViewAdapter(private val onItemClick: Consumer<Floor>) :
    RecyclerView.Adapter<FloorViewHolder>() {
    private var floors = mutableListOf<Floor>()
    fun setFloors(floors: List<Floor>) {
        this.floors = floors.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FloorListItemBinding.inflate(inflater, parent, false)
        return FloorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FloorViewHolder, position: Int) {
        val floor = floors[position]
        holder.binding.name.text = floor.name
        holder.itemView.setOnClickListener { onItemClick.accept(floor) }
    }

    override fun getItemCount(): Int {
        return floors.size
    }
}

class FloorViewHolder(val binding: FloorListItemBinding) : RecyclerView.ViewHolder(binding.root)