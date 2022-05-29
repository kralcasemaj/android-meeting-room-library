package com.jamesclark.android.androidexamplelibrary.meetingroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floor
import kotlinx.coroutines.*

class MeetingRoomViewModel(private val repository: MeetingRoomRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val floorList = MutableLiveData<List<Floor>>()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()
    var job: Job? = null

    fun getAllFloors() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getAllFloors()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        floorList.postValue(body.floors)
                        loading.value = false
                    }
                } else {
                    onError("Error : ${response.message()} ")
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