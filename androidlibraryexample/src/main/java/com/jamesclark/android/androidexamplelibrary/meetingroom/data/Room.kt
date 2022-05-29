package com.jamesclark.android.androidexamplelibrary.meetingroom.data

data class Room(
    val availability: List<Availability>,
    val id: Int,
    val name: String
)