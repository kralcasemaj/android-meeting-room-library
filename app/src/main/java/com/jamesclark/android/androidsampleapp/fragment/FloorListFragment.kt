package com.jamesclark.android.androidsampleapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jamesclark.android.androidexamplelibrary.meetingroom.MeetingRoomViewModel
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floor
import com.jamesclark.android.androidlibraryexample.app.databinding.FloorListFragmentBinding
import com.jamesclark.android.androidsampleapp.activity.MeetingRoomActivity
import com.jamesclark.android.androidsampleapp.viewadapter.FloorRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FloorListFragment(private val viewModel: MeetingRoomViewModel) : Fragment() {
    private lateinit var binding: FloorListFragmentBinding
    private val recyclerViewAdapter = FloorRecyclerViewAdapter(this::onItemClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FloorListFragmentBinding.inflate(inflater, container, false)
        binding.recyclerview.adapter = recyclerViewAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.floorList.observe(viewLifecycleOwner) { floors ->
            recyclerViewAdapter.setFloors(floors)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Log.e(FloorListFragment::class.java.simpleName, "Error loading floors: $error")
        }
        viewModel.getAllFloors()
    }

    private fun onItemClick(floor: Floor) {
        val thisActivity = activity
        if (thisActivity is MeetingRoomActivity) {
            thisActivity.showRoomList(floor)
        }
    }
}
