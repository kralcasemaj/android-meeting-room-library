package com.jamesclark.android.androidexamplelibrary.meetingroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Availability
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floor
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Room
import kotlinx.coroutines.*

class MeetingRoomViewModel(
    application: Application,
    private val repository: MeetingRoomRepository
) : AndroidViewModel(application) {
    val cachePath = getApplication<Application>().cacheDir.canonicalPath + "/" + "floors.json"
    val errorMessage = MutableLiveData<String>()
    val floorList = MutableLiveData<List<Floor>>()
    val roomList = MutableLiveData<List<Room>>()
    val availableTimesList = MutableLiveData<List<Availability>>()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()
    var job: Job? = null

    fun getAvailableTimes(rooms: List<Room>) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val roomIDSet = rooms.map { r -> r.id }.toSet()
            if (MeetingRoomAPI.isInternetConnected(getApplication())) {
                // load from API if internet is connected
                val response = repository.getAllFloorsFromAPI()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            FileUtils.writeFile(Gson().toJson(body), cachePath)
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
                val floors = repository.getAllFloorsFromLocalCache()
                withContext(Dispatchers.Main) {
                    availableTimesList.postValue(floors.flatMap { f -> f.rooms }
                        .filter { r -> roomIDSet.contains(r.id) }
                        .flatMap { r -> r.availability })
                    loading.value = false
                }
            }
        }
    }

    fun getRooms(floor: Floor) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (MeetingRoomAPI.isInternetConnected(getApplication())) {
                // load from API if internet is connected
                val response = repository.getAllFloorsFromAPI()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            FileUtils.writeFile(Gson().toJson(body), cachePath)
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
                val floors = repository.getAllFloorsFromLocalCache()
                withContext(Dispatchers.Main) {
                    roomList.postValue(floors.filter { f -> f.id == floor.id }
                        .flatMap { f -> f.rooms })
                    loading.value = false
                }
            }
        }
    }

    fun getAllRooms() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (MeetingRoomAPI.isInternetConnected(getApplication())) {
                // load from API if internet is connected
                val response = repository.getAllFloorsFromAPI()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            FileUtils.writeFile(Gson().toJson(body), cachePath)
                            roomList.postValue(body.floors.flatMap { f -> f.rooms })
                            loading.value = false
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            } else {
                // otherwise load from local cache if app is offline
                val floors = repository.getAllFloorsFromLocalCache()
                withContext(Dispatchers.Main) {
                    roomList.postValue(floors.flatMap { f -> f.rooms })
                    loading.value = false
                }
            }
        }
    }

    fun getAllFloors() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (MeetingRoomAPI.isInternetConnected(getApplication())) {
                // load from API if internet is connected
                val response = repository.getAllFloorsFromAPI()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            FileUtils.writeFile(Gson().toJson(body), cachePath)
                            floorList.postValue(body.floors)
                            loading.value = false
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            } else {
                // otherwise load from local cache if app is offline
                val floors = repository.getAllFloorsFromLocalCache()
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