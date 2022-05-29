package com.jamesclark.android.androidexamplelibrary.meetingroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MeetingRoomViewModelFactory(private val repository: MeetingRoomRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MeetingRoomViewModel::class.java)) {
            MeetingRoomViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}