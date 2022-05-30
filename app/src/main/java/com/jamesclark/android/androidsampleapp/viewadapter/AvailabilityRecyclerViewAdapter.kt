package com.jamesclark.android.androidsampleapp.viewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Availability
import com.jamesclark.android.androidlibraryexample.app.databinding.AvailabilityListItemBinding

class AvailabilityRecyclerViewAdapter :
    RecyclerView.Adapter<AvailabilityViewHolder>() {
    var availabilities = listOf<Availability>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AvailabilityListItemBinding.inflate(inflater, parent, false)
        return AvailabilityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvailabilityViewHolder, position: Int) {
        val availability = availabilities[position]
        holder.binding.timeslot.text = availability.timeslot
    }

    override fun getItemCount(): Int {
        return availabilities.size
    }
}

class AvailabilityViewHolder(val binding: AvailabilityListItemBinding) :
    RecyclerView.ViewHolder(binding.root)