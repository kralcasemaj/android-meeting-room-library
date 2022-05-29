package com.jamesclark.android.androidexamplelibrary.meetingroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floor
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floors
import kotlinx.coroutines.*
import java.io.File

class MeetingRoomViewModel(
    application: Application,
    private val repository: MeetingRoomRepository
) : AndroidViewModel(application) {
    val cachePath = getApplication<Application>().cacheDir.canonicalPath + "/" + "floors.json"
    val errorMessage = MutableLiveData<String>()
    val floorList = MutableLiveData<List<Floor>>()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()
    var job: Job? = null

    fun getAllFloors() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (MeetingRoomAPI.isInternetConnected(getApplication())) {
                // load from API if internet is connected
                val response = repository.getAllFloors()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            FileUtils.saveToCacheFile(Gson().toJson(body), cachePath)
                            floorList.postValue(body.floors)
                            loading.value = false
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            } else {
                // otherwise load from local cache if app is offline
                if (File(cachePath).exists()) {
                    val floors =
                        Gson().fromJson(FileUtils.readJsonFile(cachePath), Floors::class.java)
                    withContext(Dispatchers.Main) {
                        floorList.postValue(floors.floors)
                        loading.value = false
                    }
                } else {
                    // no cached data to load
                    floorList.postValue(emptyList())
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