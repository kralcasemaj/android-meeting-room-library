package com.jamesclark.android.androidlibraryexample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jamesclark.android.androidsampleapp.activity.MeetingRoomActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UITests {
    @JvmField
    @Rule
    var rule: ActivityScenarioRule<MeetingRoomActivity> = ActivityScenarioRule(
        MeetingRoomActivity::class.java
    )

    @Test
    fun checkGingerbreadAvailability() {
        // the test will fail if the data does not load within this time limit
        Thread.sleep(2000)

        onView(withText("Ground")).check(matches(isDisplayed()))
        onView(withText("First floor")).check(matches(isDisplayed()))
        onView(withText("Second floor")).check(matches(isDisplayed()))
        onView(withText("Third floor")).check(matches(isDisplayed()))
        onView(withText("Forth Floor")).check(matches(isDisplayed()))
        onView(withText("Second floor")).perform(click())

        onView(withText("Gingerbread")).check(matches(isDisplayed()))
        onView(withText("Honeycomb")).check(matches(isDisplayed()))
        onView(withText("Gingerbread")).perform(click())

        onView(withText("09:00")).check(matches(isDisplayed()))
        onView(withText("09:30")).check(matches(isDisplayed()))
        onView(withText("10:00")).check(matches(isDisplayed()))
        onView(withText("10:30")).check(matches(isDisplayed()))
        onView(withText("11:00")).check(matches(isDisplayed()))
        onView(withText("14:30")).check(matches(isDisplayed()))
        onView(withText("15:00")).check(matches(isDisplayed()))
        onView(withText("15:30")).check(matches(isDisplayed()))
    }
}