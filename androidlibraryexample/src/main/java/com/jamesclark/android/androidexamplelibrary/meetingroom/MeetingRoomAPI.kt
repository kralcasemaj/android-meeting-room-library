package com.jamesclark.android.androidexamplelibrary.meetingroom

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
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

        fun isInternetConnected(context: Context): Boolean {
            val connMgr =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            connMgr ?: return false
            val network: Network = connMgr.activeNetwork ?: return false
            val capabilities = connMgr.getNetworkCapabilities(network)
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        fun getInstance(baseUrl: String): MeetingRoomAPI {
            val currentInstance = instance
            return if (currentInstance == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val newInstance = retrofit.create(MeetingRoomAPI::class.java)
                instance = newInstance
                newInstance
            } else {
                currentInstance
            }
        }

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