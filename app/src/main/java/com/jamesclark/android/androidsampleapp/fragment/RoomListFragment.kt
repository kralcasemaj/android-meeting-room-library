package com.jamesclark.android.androidsampleapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floor
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Room
import com.jamesclark.android.androidlibraryexample.app.databinding.RoomListFragmentBinding
import com.jamesclark.android.androidsampleapp.activity.MeetingRoomActivity
import com.jamesclark.android.androidsampleapp.viewadapter.RoomRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomListFragment(private val floor: Floor) : Fragment() {
    private lateinit var binding: RoomListFragmentBinding
    private val recyclerViewAdapter = RoomRecyclerViewAdapter(this::onItemClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RoomListFragmentBinding.inflate(inflater, container, false)
        binding.recyclerview.adapter = recyclerViewAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewAdapter.setRooms(floor.rooms)
    }

    private fun onItemClick(room: Room) {
        val thisActivity = activity
        if (thisActivity is MeetingRoomActivity) {
            thisActivity.showAvailabilityList(room)
        }
    }
}