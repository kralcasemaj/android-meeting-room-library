package com.jamesclark.android.androidexamplelibrary.meetingroom

import com.google.gson.Gson
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floor
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floors
import java.io.File

class MeetingRoomRepository(val api: MeetingRoomAPI) {
    suspend fun getAllFloorsFromAPI() = api.getAllFloors()

    fun getAllFloorsFromLocalCache(cachePath: String): List<Floor> {
        return if (File(cachePath).exists()) {
            val floors =
                Gson().fromJson(FileUtils.readFile(cachePath), Floors::class.java)
            floors.floors
        } else {
            emptyList()
        }
    }
}
