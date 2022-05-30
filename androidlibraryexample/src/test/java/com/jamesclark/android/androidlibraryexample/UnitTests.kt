package com.jamesclark.android.androidlibraryexample

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.jamesclark.android.androidexamplelibrary.meetingroom.MeetingRoomAPI
import com.jamesclark.android.androidexamplelibrary.meetingroom.MeetingRoomRepository
import com.jamesclark.android.androidexamplelibrary.meetingroom.MeetingRoomViewModel
import com.jamesclark.android.androidexamplelibrary.meetingroom.data.Floors
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class UnitTests {
    private val server: MockWebServer = MockWebServer()
    private val MOCK_WEBSERVER_PORT = 8000
    lateinit var api: MeetingRoomAPI
    lateinit var repository: MeetingRoomRepository
    lateinit var viewModel: MeetingRoomViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        server.start(MOCK_WEBSERVER_PORT)
        api = MeetingRoomAPI.getInstance(server.url("/").toString())
        repository = MeetingRoomRepository(api)
        viewModel = MeetingRoomViewModel(repository)
    }

    @Test
    fun `MeetingRoomAPI getAllFloors`() {
        server.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("floors_mock_response.json").content))
        }
        val response = runBlocking { repository.getAllFloorsFromAPI() }
        assertTrue(response.isSuccessful)

        val floors = response.body()
        assertNotNull(floors)
        assertEquals(floors!!.floors.size, 5)
        val forthFloor = floors.floors.firstOrNull { f -> f.id == 5 }
        assertNotNull(forthFloor)
        assertEquals(forthFloor!!.name, "Forth Floor")
        val kitkatRoom = forthFloor.rooms.firstOrNull { r -> r.name == "Kitkat" }
        assertNotNull(kitkatRoom)
        assertEquals(kitkatRoom!!.id, 9)
        val kitkatAvailability = kitkatRoom.availability
        assertTrue(kitkatAvailability.any { a -> a.timeslot == "15:00" && a.id == 16 })
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `MeetingRoomViewModel getAllFloors`() = runTest {
        server.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("floors_mock_response.json").content))
        }

        viewModel.getAllFloors()
        val floors = viewModel.floorList.getOrAwaitValue()
        assertNotNull(floors)
        assertEquals(floors!!.size, 5)
        val forthFloor = floors.firstOrNull { f -> f.id == 5 }
        assertNotNull(forthFloor)
        assertEquals(forthFloor!!.name, "Forth Floor")
        val kitkatRoom = forthFloor.rooms.firstOrNull { r -> r.name == "Kitkat" }
        assertNotNull(kitkatRoom)
        assertEquals(kitkatRoom!!.id, 9)
        val kitkatAvailability = kitkatRoom.availability
        assertTrue(kitkatAvailability.any { a -> a.timeslot == "15:00" && a.id == 16 })
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `MeetingRoomViewModel getAllRooms`() = runTest {
        server.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("floors_mock_response.json").content))
        }

        viewModel.getAllRooms()
        val rooms = viewModel.roomList.getOrAwaitValue()
        assertNotNull(rooms)
        assertEquals(rooms!!.size, 10)
        val kitkatRoom = rooms.firstOrNull { r -> r.name == "Kitkat" }
        assertNotNull(kitkatRoom)
        assertEquals(kitkatRoom!!.id, 9)
        val kitkatAvailability = kitkatRoom.availability
        assertTrue(kitkatAvailability.any { a -> a.timeslot == "15:00" && a.id == 16 })
        val honeycombRoom = rooms.firstOrNull { r -> r.name == "Honeycomb" }
        assertNotNull(honeycombRoom)
        assertEquals(honeycombRoom!!.id, 6)
        val honeycombAvailability = honeycombRoom.availability
        assertTrue(honeycombAvailability.any { a -> a.timeslot == "14:30" && a.id == 12 })
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `MeetingRoomViewModel getRooms(floor)`() = runTest {
        server.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("floors_mock_response.json").content))
        }
        val floors = Gson().fromJson(
            MockResponseFileReader("floors_mock_response.json").content,
            Floors::class.java
        ).floors
        val secondFloor = floors.first { f -> f.id == 3 }
        viewModel.getRooms(secondFloor)
        val rooms = viewModel.roomList.getOrAwaitValue()
        assertNotNull(rooms)
        assertEquals(rooms!!.size, 2)
        val honeycombRoom = rooms.firstOrNull { r -> r.name == "Honeycomb" }
        assertNotNull(honeycombRoom)
        assertEquals(honeycombRoom!!.id, 6)
        val honeycombAvailability = honeycombRoom.availability
        assertTrue(honeycombAvailability.any { a -> a.timeslot == "14:30" && a.id == 12 })

        val gingerbreadRoom = rooms.firstOrNull { r -> r.name == "Gingerbread" }
        assertNotNull(gingerbreadRoom)
        assertEquals(gingerbreadRoom!!.id, 5)
        val gingerbreadAvailability = gingerbreadRoom.availability
        assertTrue(gingerbreadAvailability.any { a -> a.timeslot == "09:30" && a.id == 2 })
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `MeetingRoomViewModel getAvailableTimes(room)`() = runTest {
        server.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("floors_mock_response.json").content))
        }
        val rooms = Gson().fromJson(
            MockResponseFileReader("floors_mock_response.json").content,
            Floors::class.java
        ).floors.flatMap { f -> f.rooms }
            .filter { r -> r.name == "Gingerbread" || r.name == "Froyo" }
        viewModel.getAvailableTimes(rooms)
        val availableTimes = viewModel.availableTimesList.getOrAwaitValue()
        assertNotNull(availableTimes)
        assertEquals(availableTimes.size, 10)
        assertNotNull(availableTimes.firstOrNull { a -> a.timeslot == "09:00" && a.id == 1 })
        assertNotNull(availableTimes.firstOrNull { a -> a.timeslot == "10:00" && a.id == 3 })
    }

    @After
    fun shutdown() {
        server.shutdown()
    }
}