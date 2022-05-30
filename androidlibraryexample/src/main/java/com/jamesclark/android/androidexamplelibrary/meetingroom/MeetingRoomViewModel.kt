package com.jamesclark.android.androidexamplelibrary.meetingroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Availability
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floor
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MeetingRoomViewModel(
    private val repository: MeetingRoomRepository,
    private val isInternetConnected: Boolean = true,
    val cachePath: String = ""
) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val floorList = MutableLiveData<List<Floor>>()
    val roomList = MutableLiveData<List<Room>>()
    val availableTimesList = MutableLiveData<List<Availability>>()
    val loading = MutableLiveData<Boolean>()
    var job: Job? = null

    /**
     * Get a list of available times for
     * the specified rooms
     */
    fun getAvailableTimes(rooms: List<Room>) {
        job = viewModelScope.launch {
            val roomIDSet = rooms.map { r -> r.id }.toSet()
            if (isInternetConnected) {
                // load from API if internet is connected
                val response = repository.getAllFloorsFromAPI()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            if (cachePath.isNotEmpty()) {
                                FileUtils.writeFile(Gson().toJson(body), cachePath)
                            }
                            availableTimesList.postValue(body.floors.flatMap { f -> f.rooms }
                                .filter { r -> roomIDSet.contains(r.id) }
                                .flatMap { r -> r.availability })
                            loading.value = false
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            } else {
                // otherwise load from local cache if app is offline
                val floors = repository.getAllFloorsFromLocalCache(cachePath)
                withContext(Dispatchers.Main) {
                    availableTimesList.postValue(floors.flatMap { f -> f.rooms }
                        .filter { r -> roomIDSet.contains(r.id) }
                        .flatMap { r -> r.availability })
                    loading.value = false
                }
            }
        }
    }

    /**
     * Get a list of floors for
     * the specified floor
     */
    fun getRooms(floor: Floor) {
        job = viewModelScope.launch {
            if (isInternetConnected) {
                // load from API if internet is connected
                val response = repository.getAllFloorsFromAPI()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            if (cachePath.isNotEmpty()) {
                                FileUtils.writeFile(Gson().toJson(body), cachePath)
                            }
                            roomList.postValue(body.floors.filter { f -> f.id == floor.id }
                                .flatMap { f -> f.rooms })
                            loading.value = false
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            } else {
                // otherwise load from local cache if app is offline
                val floors = repository.getAllFloorsFromLocalCache(cachePath)
                withContext(Dispatchers.Main) {
                    roomList.postValue(floors.filter { f -> f.id == floor.id }
                        .flatMap { f -> f.rooms })
                    loading.value = false
                }
            }
        }
    }

    /**
     * Get a list of all rooms
     */
    fun getAllRooms() {
        job = viewModelScope.launch {
            if (isInternetConnected) {
                // load from API if internet is connected
                val response = repository.getAllFloorsFromAPI()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            if (cachePath.isNotEmpty()) {
                                FileUtils.writeFile(Gson().toJson(body), cachePath)
                            }
                            roomList.postValue(body.floors.flatMap { f -> f.rooms })
                            loading.value = false
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            } else {
                // otherwise load from local cache if app is offline
                val floors = repository.getAllFloorsFromLocalCache(cachePath)
                withContext(Dispatchers.Main) {
                    roomList.postValue(floors.flatMap { f -> f.rooms })
                    loading.value = false
                }
            }
        }
    }

    /**
     * Get a list of all floors
     */
    fun getAllFloors() {
        job = viewModelScope.launch {
            if (isInternetConnected) {
                // load from API if internet is connected
                val response = repository.getAllFloorsFromAPI()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            if (cachePath.isNotEmpty()) {
                                FileUtils.writeFile(Gson().toJson(body), cachePath)
                            }
                            floorList.postValue(body.floors)
                            loading.value = false
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            } else {
                // otherwise load from local cache if app is offline
                val floors = repository.getAllFloorsFromLocalCache(cachePath)
                withContext(Dispatchers.Main) {
                    floorList.postValue(floors)
                    loading.value = false
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}