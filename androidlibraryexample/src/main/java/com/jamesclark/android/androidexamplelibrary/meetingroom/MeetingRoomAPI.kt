package com.jamesclark.android.androidexamplelibrary.meetingroom

import android.content.Context
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floors
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.*

interface MeetingRoomAPI {

    /**
    returns a list of floors
    with rooms and available
    time slots to book meetings
     **/
    @GET("/v3/799f5235-d697-4a28-829f-0ff0d23be9dc")
    suspend fun getAllFloors(): Response<Floors>

    companion object {
        private var instance: MeetingRoomAPI? = null

        @JvmStatic
        fun getInstance(context: Context): MeetingRoomAPI {
            val currentInstance = instance
            return if (currentInstance == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(getApiBaseUri(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val newInstance = retrofit.create(MeetingRoomAPI::class.java)
                instance = newInstance
                newInstance
            } else {
                currentInstance
            }
        }

        @JvmStatic
        fun getApiBaseUri(context: Context): String {
            return getProperty(
                "api.properties",
                "apiBaseUri",
                context
            )
        }

        private fun getProperty(propertiesFile: String, key: String, context: Context): String {
            val properties = Properties()
            val assetManager = context.assets
            assetManager.open(propertiesFile).use { inputStream ->
                properties.load(inputStream)
                return properties.getProperty(key)
            }
        }
    }
}