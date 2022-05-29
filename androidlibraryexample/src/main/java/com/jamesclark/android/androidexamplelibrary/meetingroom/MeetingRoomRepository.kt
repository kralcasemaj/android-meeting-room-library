package com.jamesclark.android.androidexamplelibrary.meetingroom

class MeetingRoomRepository(private val api: MeetingRoomAPI) {
    suspend fun getAllFloors() = api.getAllFloors()
}
