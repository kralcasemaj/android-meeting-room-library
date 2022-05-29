package com.jamesclark.android.androidexamplelibrary.meetingroom.data

data class Floor(
    val id: Int,
    val name: String,
    val rooms: List<Room>
)