package com.jamesclark.android.androidexamplelibrary.meetingroom

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MeetingRoomViewModelFactory(
    private val application: Application,
    private val repository: MeetingRoomRepository
) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MeetingRoomViewModel::class.java)) {
            MeetingRoomViewModel(application, repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}