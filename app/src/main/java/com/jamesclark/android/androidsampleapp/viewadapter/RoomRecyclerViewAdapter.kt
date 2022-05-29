package com.jamesclark.android.androidsampleapp.viewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Room
import com.jamesclark.android.androidlibraryexample.app.databinding.RoomListItemBinding
import java.util.function.Consumer

class RoomRecyclerViewAdapter(private val onItemClick: Consumer<Room>) :
    RecyclerView.Adapter<RoomViewHolder>() {
    private var rooms = listOf<Room>()
    fun setRooms(rooms: List<Room>) {
        this.rooms = rooms.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RoomListItemBinding.inflate(inflater, parent, false)
        return RoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.binding.name.text = room.name
        holder.itemView.setOnClickListener { onItemClick.accept(room) }
    }

    override fun getItemCount(): Int {
        return rooms.size
    }
}

class RoomViewHolder(val binding: RoomListItemBinding) : RecyclerView.ViewHolder(binding.root)