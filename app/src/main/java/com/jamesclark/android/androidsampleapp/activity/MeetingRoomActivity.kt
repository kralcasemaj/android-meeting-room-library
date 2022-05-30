package com.jamesclark.android.androidsampleapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jamesclark.android.androidexamplelibrary.meetingroom.MeetingRoomRepository
import com.jamesclark.android.androidexamplelibrary.meetingroom.MeetingRoomViewModel
import com.jamesclark.android.androidexamplelibrary.meetingroom.MeetingRoomViewModelFactory
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floor
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Room
import com.jamesclark.android.androidlibraryexample.app.R
import com.jamesclark.android.androidlibraryexample.app.databinding.MeetingRoomActivityBinding
import com.jamesclark.android.androidsampleapp.fragment.AvailabilityListFragment
import com.jamesclark.android.androidsampleapp.fragment.FloorListFragment
import com.jamesclark.android.androidsampleapp.fragment.RoomListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetingRoomActivity : AppCompatActivity() {
    private lateinit var viewModel: MeetingRoomViewModel
    private lateinit var binding: MeetingRoomActivityBinding
    private var shownFragment = FloorListFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MeetingRoomActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            MeetingRoomViewModelFactory(
                application,
                MeetingRoomRepository(application.applicationContext)
            )
        )[MeetingRoomViewModel::class.java]

        showFloorList()
    }

    private fun showFloorList() {
        title = getString(R.string.floors)
        shownFragment = FloorListFragment::class.java.simpleName
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, FloorListFragment(viewModel))
            .commit()
    }

    internal fun showRoomList(floor: Floor) {
        title = String.format(getString(R.string.rooms), floor.name)
        shownFragment = RoomListFragment::class.java.simpleName
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, RoomListFragment(floor))
            .commit()
    }

    internal fun showAvailabilityList(room: Room) {
        title = String.format(getString(R.string.available_times), room.name)
        shownFragment = AvailabilityListFragment::class.java.simpleName
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, AvailabilityListFragment(room))
            .commit()
    }

    override fun onBackPressed() {
        if (shownFragment == AvailabilityListFragment::class.java.simpleName
            || shownFragment == RoomListFragment::class.java.simpleName
        ) {
            showFloorList()
        } else {
            super.onBackPressed()
        }
    }
}