package com.jamesclark.android.androidsampleapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Room
import com.jamesclark.android.androidlibraryexample.app.databinding.AvailabilityListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvailabilityListFragment(private val room: Room) : Fragment() {
    private lateinit var binding: AvailabilityListFragmentBinding
    private val recyclerViewAdapter = AvailabilityRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AvailabilityListFragmentBinding.inflate(inflater, container, false)
        binding.recyclerview.adapter = recyclerViewAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewAdapter.availabilities = room.availability
    }
}