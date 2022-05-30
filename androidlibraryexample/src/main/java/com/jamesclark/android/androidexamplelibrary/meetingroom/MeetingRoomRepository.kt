package com.jamesclark.android.androidexamplelibrary.meetingroom

import android.content.Context
import com.google.gson.Gson
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floor
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floors
import java.io.File

class MeetingRoomRepository(
    private val context: Context
) {
    private val api = MeetingRoomAPI.getInstance(context)

    suspend fun getAllFloorsFromAPI() = api.getAllFloors()

    fun getAllFloorsFromLocalCache(): List<Floor> {
        val cachePath = context.cacheDir.canonicalPath + "/" + "floors.json"
        return if (File(cachePath).exists()) {
            val floors =
                Gson().fromJson(FileUtils.readFile(cachePath), Floors::class.java)
            floors.floors
        } else {
            emptyList()
        }
    }
}
